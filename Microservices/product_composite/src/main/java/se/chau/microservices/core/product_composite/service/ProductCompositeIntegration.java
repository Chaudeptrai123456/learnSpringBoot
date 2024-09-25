package se.chau.microservices.core.product_composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.chau.microservices.api.core.Feature.Feature;
import se.chau.microservices.api.core.Feature.FeatureForSearchPro;
import se.chau.microservices.api.core.Feature.FeatureService;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.core.recommandation.RecommendationService;
import se.chau.microservices.api.core.review.Review;
import se.chau.microservices.api.core.review.ReviewService;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.util.http.HttpErrorInfo;
import static java.util.logging.Level.FINE;
import static reactor.core.publisher.Flux.empty;
import static se.chau.microservices.api.event.Event.Type.CREATE;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

@Component
public class ProductCompositeIntegration implements ProductService, ReviewService,RecommendationService, FeatureService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);
    private final WebClient webClient;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;
    private final StreamBridge streamBridge;
    private final Scheduler publishEventScheduler;
    private final String featureServiceUrl;

    private final String getProductByfeaUrl;


    private static final String PRODUCT_SERVICE_URL = "http://product:8080";
    private static final String RECOMMENDATION_SERVICE_URL = "http://recommendation:8080";
    private static final String REVIEW_SERVICE_URL = "http://review:8080";
    @Autowired
    public ProductCompositeIntegration(
            @Qualifier("publishEventScheduler") Scheduler publishEventScheduler,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") int productServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") int recommendationServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") int reviewServicePort, WebClient.Builder webClient,
            @Value("${app.feature-service.host}") String featureHost,
            @Value("${app.feature-service.port}") String featurePort,
            ObjectMapper mapper,
            StreamBridge streamBridge) {
        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClient.build();
        this.mapper = mapper;
        this.streamBridge = streamBridge;
        this.getProductByfeaUrl = "http://" + featureHost + ":" + featurePort + "/feature/product";
        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
        featureServiceUrl = "http://" + featureHost + ":" + featurePort + "/feature?productId=";
    }
    @Override
    public Mono<Product> createProduct(Product product) {
        return Mono.fromCallable(() -> {
            sendMessage("products-out-0",new Event(CREATE, product.getProductId(), product));
            return product;
        }).subscribeOn(publishEventScheduler);
    }
    @Override
    public Mono<Product> getProduct(int productId) {
        String url = PRODUCT_SERVICE_URL + "/product/" + productId;
        return webClient.get().uri(url).retrieve()
                .bodyToMono(Product.class)
                .log(LOG.getName(), FINE)
                .onErrorMap(WebClientResponseException.class,
                        this::handleException
                );
    }
    @Override
    public Mono<Recommendation> createRecommendation(Recommendation recommendation) {
        return Mono.fromCallable(() -> {
            sendMessage("recommendations-out-0",new Event(CREATE, recommendation.getRecommendationId(), recommendation));
            return recommendation;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {

        return null;
    }
    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
    @Override
    public Flux<Recommendation> getRecommendations(int productId) {
        String url = RECOMMENDATION_SERVICE_URL + "/recommendation?productId=" + productId;
        return webClient.get().uri(url).retrieve()
                .bodyToFlux(Recommendation.class)
                .log(LOG.getName(), FINE)
                .onErrorResume(error -> empty());
    }


    @Override
    public Flux<Review> getReviews(int productId) {
        String url = REVIEW_SERVICE_URL + "/review?productId=" + productId;
        LOG.debug("Will call the getReviews API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToFlux(Review.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }



    @Override
    public Mono<Review> createReview(Review body) {
        LOG.debug("test create review " + body.getReviewId());
        return Mono.fromCallable(() -> {
            sendMessage("reviews-out-0", new Event(CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }
    public Mono<Health> getProductHealth() {
        return getHealth(PRODUCT_SERVICE_URL);
    }

    public Mono<Health> getRecommendationHealth() {
        return getHealth(RECOMMENDATION_SERVICE_URL);
    }

    public Mono<Health> getReviewHealth() {
        return getHealth(REVIEW_SERVICE_URL);
    }

    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        LOG.debug("Will call the Health API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log(LOG.getName(), FINE);
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message<Event> message = MessageBuilder.withPayload(event).setHeader("partitionKey", event.getKey()).build();
        streamBridge.send(bindingName, message);
    }

    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException wcre)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        switch (HttpStatus.resolve(wcre.getStatusCode().value())) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(wcre));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(wcre));

            case null:
                break;
            default:
                LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
                return ex;
        }
        return ex;
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

    @Override
    public Flux<Feature> getProductByFeature(FeatureForSearchPro feature) throws HttpClientErrorException {
        return webClient.post().uri(getProductByfeaUrl).contentType(MediaType.APPLICATION_JSON).bodyValue(feature).retrieve().bodyToFlux(Feature.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }


    @Override
    public Mono<Feature> createFeatureForProduct(Feature feature) throws HttpClientErrorException {
        return Mono.fromCallable(() -> {
            sendMessage("features-out-0", new Event(CREATE, feature.getFeatureId(), feature));
            return feature;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Flux<Feature> getFeatureOfProduct(int productId) {
        String url = featureServiceUrl + productId;
        LOG.debug("Will call the getReviews API on URL: {}", url);
        //        return webClient.get().uri(url).retrieve().bodyToFlux(Review.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
        return webClient.get().uri(url).retrieve().bodyToFlux(Feature.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());    }
}