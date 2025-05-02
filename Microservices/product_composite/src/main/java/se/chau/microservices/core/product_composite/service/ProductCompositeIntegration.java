package se.chau.microservices.core.product_composite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import se.chau.microservices.api.core.order.ProductOrder;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductService;
import se.chau.microservices.api.core.product.ProductUpdate;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.core.recommandation.RecommendationService;
import se.chau.microservices.api.core.review.Review;
import se.chau.microservices.api.core.review.ReviewService;
import se.chau.microservices.api.discount.Discount;
import se.chau.microservices.api.discount.DiscountService;
import se.chau.microservices.api.event.Event;
import se.chau.microservices.api.exception.InvalidInputException;
import se.chau.microservices.api.exception.NotFoundException;
import se.chau.microservices.core.product_composite.service.Cache.RedisService;
import se.chau.microservices.util.http.HttpErrorInfo;
import se.chau.microservices.util.http.ServiceUtil;

import java.io.IOException;
import java.util.List;

import static java.util.logging.Level.FINE;
import static reactor.core.publisher.Flux.empty;
import static se.chau.microservices.api.event.Event.Type.CREATE;
import static se.chau.microservices.api.event.Event.Type.UPDATE;

@Component
public class ProductCompositeIntegration implements ProductService, ReviewService, RecommendationService, FeatureService, DiscountService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);
    private final WebClient webClient;
    private final ObjectMapper mapper;
    private final StreamBridge streamBridge;
    private final Scheduler publishEventScheduler;
    private final ServiceUtil serviceUtil;
    @Value("${uri.service.product}")
    private String PRODUCT_SERVICE_URL;
    @Value("${uri.service.recommendation}")
    private String RECOMMENDATION_SERVICE_URL;
    @Value("${uri.service.review}")
    private String REVIEW_SERVICE_URL;
    @Value("${uri.service.feature}")
    private String FEATURE_SERVICE_URL;
    @Value("${uri.service.discount}")
    private String DISCOUNT_SERVICE_URL;

    private final RedisService redisService;

    @Autowired
    public ProductCompositeIntegration(
            @Qualifier("publishEventScheduler") Scheduler publishEventScheduler,
            WebClient webClient,
            ObjectMapper mapper,
            StreamBridge streamBridge,
            ServiceUtil serviceUtil, RedisService redisService) {
        this.webClient = webClient;
        this.publishEventScheduler = publishEventScheduler;
        this.mapper = mapper;
        this.streamBridge = streamBridge;
        this.serviceUtil = serviceUtil;
        this.redisService = redisService;
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        return Mono.fromCallable(() -> {
            sendMessage("products-out-0", new Event(CREATE, product.getProductId(), product));
            return product;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Flux<Product> getProductPage(int page) {
        LOG.debug("get product page");
        String url = PRODUCT_SERVICE_URL + "/product/page/" + page;
        return webClient.get().uri(url).retrieve()
                .bodyToFlux(Product.class)
                .log(LOG.getName(), FINE)
                .onErrorMap(WebClientResponseException.class,
                        this::handleException
                );
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "getProductFallbackValue") // Giữ nguyên
    @Retry(name = "product")
    public Mono<Product> getProduct(int productId) {
        LOG.debug("test get product " + PRODUCT_SERVICE_URL);
        String url = PRODUCT_SERVICE_URL + "/product/" + productId;
        return webClient.get().uri(url).retrieve()
                .bodyToMono(Product.class)
                .log(LOG.getName(), FINE)
                .doOnError(err -> LOG.error(err.getMessage()))
                ;

    }

    private Mono<Product> getProductFallbackValue(int productId, Throwable ex) { // Đã thêm Throwable ex
        LOG.warn("Fallback cho getProduct, productId = {}, lỗi: {}", productId, ex.getMessage());
        // Thêm logic xử lý lỗi cụ thể nếu cần
        return Mono.just(new Product(productId, "Fallback product " + productId, productId, 13.0, serviceUtil.getServiceAddress()));
    }



    @Override
    public Mono<Recommendation> createRecommendation(Recommendation recommendation) {
        return Mono.fromCallable(() -> {
            sendMessage("recommendations-out-0", new Event(CREATE, recommendation.getRecommendationId(), recommendation));
            return recommendation;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {

        return null;
    }

    @Override
    public Mono<Product> updateProduct(ProductUpdate product, int productId) {
        if (productId == 13) {
            String errMsg = "Product Id: " + productId + " not found in fallback cache!";
            LOG.warn(errMsg);
            return Mono.error(new NotFoundException(errMsg));
        }

        sendMessage("products-out-0", new Event(UPDATE, productId, product));

        return Mono.fromRunnable(() -> {
                    redisService.delete(productId);
                    LOG.info("Deleted product cache for productId={}", productId);
                })
                .then(this.getProduct(productId));
    }

    @Override
    public Mono<Double> sumCost(List<ProductOrder> list) {
        return this.sumCost(list);
    }


    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

    @Override
    @CircuitBreaker(name = "review", fallbackMethod = "getReviewsFallbackValue")
    public Flux<Recommendation> getRecommendations(int productId) {
        String url = RECOMMENDATION_SERVICE_URL + "/recommendation?productId=" + productId;
        LOG.debug("Will call the getRecommendations API on URL: {}", url);
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Recommendation.class)
                .log(LOG.getName(), FINE)
                .doOnError(error -> LOG.error("Error while fetching recommendations: {}", error.getMessage()))
                .onErrorResume(error -> Flux.empty());
    }
    //Fallback cho getRecommendations
    private Flux<Recommendation> getRecommendationsFallbackValue(int productId, Throwable ex) {
        LOG.warn("Fallback cho getRecommendations, productId = {}, lỗi: {}", productId, ex.getMessage());
        return Flux.empty();
    }

    @Override
    @CircuitBreaker(name = "review", fallbackMethod = "getReviewsFallbackValue")
    public Flux<Review> getReviews(int productId) {
        String url = REVIEW_SERVICE_URL + "/review?productId=" + productId;
        LOG.debug("Will call the getReviews API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToFlux(Review.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }
    //Fallback cho getReviews
    private Flux<Review> getReviewsFallbackValue(int productId, Throwable ex) {
        LOG.warn("Fallback cho getReviews, productId = {}, lỗi: {}", productId, ex.getMessage());
        return Flux.empty();
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

    public Mono<Health> getFeatureHealth() {
        return getHealth(FEATURE_SERVICE_URL);
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
    @CircuitBreaker(name = "product", fallbackMethod = "getProductByFeatureFallbackValue") // Đã thêm fallbackMethod
    @Retry(name = "product")
    public Flux<Feature> getProductByFeature(FeatureForSearchPro feature) throws HttpClientErrorException {
        String url = FEATURE_SERVICE_URL + "/feature/product";
        return webClient.post().uri(url).contentType(MediaType.APPLICATION_JSON).bodyValue(feature).retrieve().bodyToFlux(Feature.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }

    private Flux<Feature> getProductByFeatureFallbackValue(FeatureForSearchPro feature, Throwable ex) {
        LOG.warn("Fallback cho getProductByFeature, feature: {}, lỗi: {}", feature, ex.getMessage());
        return Flux.empty();
    }


    @Override
    public Mono<Feature> createFeatureForProduct(Feature feature) throws HttpClientErrorException {
        return Mono.fromCallable(() -> {
            sendMessage("features-out-0", new Event(CREATE, feature.getFeatureId(), feature));
            return feature;
        }).subscribeOn(publishEventScheduler);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "getFeatureOfProductFallbackValue") // Đã thêm fallbackMethod
    @Retry(name = "product")
    public Flux<Feature> getFeatureOfProduct(int productId) {
        String url = FEATURE_SERVICE_URL + "/feature?productId=" + productId;
        LOG.debug("Will call the getReviews API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToFlux(Feature.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }

    private Flux<Feature> getFeatureOfProductFallbackValue(int productId, Throwable ex) {  // Thêm phương thức fallback cho getFeatureOfProduct
        LOG.warn("Fallback cho getFeatureOfProduct, productId: {}, lỗi: {}", productId, ex.getMessage());
        return Flux.empty(); // hoặc trả về giá trị mặc định
    }

    @Override
    public Mono<Feature> getFeatureByName(String name) {
        return null;
    }

    public String fallback(Exception e) {
        return "Fallback response: Service is unavailable." + e.getMessage();
    }


    @Override
    public Mono<Discount> createDiscount(Discount discount) {
        return null;
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "getDiscountOfProFallbackValue")
    @Retry(name = "product")
    public Flux<Discount> getDiscountOfPro(int productId) {
        String url = DISCOUNT_SERVICE_URL + "/discount/product/" + productId;
        LOG.debug("Will call the getReviews API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToFlux(Discount.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }
    private Flux<Discount> getDiscountOfProFallbackValue(int productId, Throwable ex) {
        LOG.warn("Fallback cho getDiscountOfPro, productId: {}, lỗi: {}", productId, ex.getMessage());
        return Flux.empty();
    }
}
