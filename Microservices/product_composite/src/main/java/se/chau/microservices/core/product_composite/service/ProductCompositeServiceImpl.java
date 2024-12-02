package se.chau.microservices.core.product_composite.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.composite.product.*;
import se.chau.microservices.api.core.Feature.Feature;
import se.chau.microservices.api.core.Feature.FeatureForSearchPro;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.product.ProductFeature;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.core.review.Review;
import se.chau.microservices.core.product_composite.service.Cache.RedisService;
import se.chau.microservices.core.product_composite.tracing.ObservationUtil;
import se.chau.microservices.util.http.ServiceUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.logging.Level.FINE;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);
    private final ObservationUtil observationUtil;
    private final SecurityContext nullSecCtx = new SecurityContextImpl();
    private final RedisService redisService;
    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;
    @Autowired
    public ProductCompositeServiceImpl(ObservationUtil observationUtil, RedisService redisService, ServiceUtil serviceUtil, ProductCompositeIntegration integration ) {
        this.observationUtil = observationUtil;
        this.redisService = redisService;
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public Mono<Void> createProduct(ProductAggregate body) {

        return observationWithProductInfo(body.getProductId(), () -> createProductInternal(body));
    }
    private Mono<Void> createProductInternal(ProductAggregate body) {
        try {
            List<Mono> monoList = new ArrayList<>();
            LOG.info("Will create a new composite entity for product.id: {}", body.getProductId());
            Product product = new Product(body.getProductId(), body.getName(), body.getQuantity(),body.getCost() ,null);
            monoList.add(integration.createProduct(product));
            if (body.getRecommendationSummaryList() != null) {
                body.getRecommendationSummaryList().forEach(r -> {
                    Recommendation recommendation = new Recommendation(body.getProductId(), r.getRecommendationId(), r.getAuthor(), r.getRate(), r.getContent(), null);
                    monoList.add(integration.createRecommendation(recommendation));
                });
            }
            if (body.getReviewSummaryList() != null) {
                body.getReviewSummaryList().forEach(r -> {
                    Review review = new Review(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
                    monoList.add(integration.createReview(review));
                });
            }
            if (body.getFeatureSummaryList() != null) {
                body.getFeatureSummaryList().forEach(f ->{
                    Feature feature = new Feature(f.getFeatureId(), f.getProductId(), f.getName(),f.getDescription(), serviceUtil.getServiceAddress());
                    monoList.add(integration.createFeatureForProduct(feature));
                    LOG.debug("test create feature " + feature.getName());
                });
            }
            return Mono.zip(r -> "", monoList.toArray(new Mono[0]))
                    .doOnError(ex -> LOG.warn("createCompositeProduct failed: {}", ex.toString()))
                    .then();

        } catch (RuntimeException re) {
            LOG.warn("createCompositeProduct failed: {}", re.toString());
            throw re;
        }
    }
    private <T> T observationWithProductInfo(int productInfo, Supplier<T> supplier) {
        return observationUtil.observe(
                "composite observation",
                "product info",
                "productId",
                String.valueOf(productInfo),
                supplier);
    }
    @Override
    public Mono<ProductAggregate> getProduct(int productId) {
        var key = "productAggregate:"+productId;
        return redisService.get(key,ProductAggregate.class).switchIfEmpty(
            observationWithProductInfo(productId, () -> getProductInternal(productId)).doOnSuccess(productAggregate -> redisService.set(key,ProductAggregate.class,3600L))
        );
    }
    private Mono<ProductAggregate> getProductInternal(int productId) {
        return observationWithProductInfo(productId, () -> {
            LOG.info("Will get composite product info for product.id={}", productId);
            return  Mono.zip(
                            values -> createProductAggregate(
                                    (SecurityContext) values[0],
                                    (Product) values[1],
                                    (List<Recommendation>) values[2],
                                    (List<Review>) values[3],
                                    (List<Feature>) values[4],
                                    serviceUtil.getServiceAddress()),
                            getSecurityContextMono(),
                            integration.getProduct(productId),
                            integration.getRecommendations(productId).collectList(),
                            integration.getReviews(productId).collectList(),
                            integration.getFeatureOfProduct(productId).collectList())
                    .doOnError(ex ->
                            LOG.warn("getCompositeProduct failed: {}",
                                    ex.toString()))
                    .log(LOG.getName(), FINE);
            }
        );

    }

    @Override
    public List<Product> getProductByFeature(@RequestBody List<FeatureForSearchPro> feature) throws EnumConstantNotPresentException {
        List<Product> result = new ArrayList<>();
        feature.forEach(index -> {
            Objects.requireNonNull(integration.getProductByFeature(index).collectList().block()).forEach(f -> {
                result.add(integration.getProduct(f.getProductId()).block());
            });

        });
        return result;
    }
    @Override
    public  void deleteProduct(int productId) {
        integration.deleteProduct(productId);
    }

    @Override
    public Flux<ProductFeature> getProductFeaturePage(int page) throws JsonProcessingException {
        var key = "page:"+page;
        return redisService.getFlux(key, ProductFeature.class).switchIfEmpty(
                observationWithProductInfo(page, () -> getProductFeaturePageInternal(page))
        );
    }
    private Flux<ProductFeature> getProductFeaturePageInternal(int page){
        Flux<Product> list = integration.getProductPage(page);  // Get the Flux of products
        var key = "page:"+page;
        return list.flatMap(product ->
                integration.getFeatureOfProduct(product.getProductId())  // Fetch features for each product
                        .collectList()  // Collect features into a List
                        .map(features -> {
                            List<FeatureSummary> featureList = (features == null) ? null : features.stream()
                                    .map(r -> new FeatureSummary(r.getFeatureId(), r.getName(), r.getDescription(), r.getProductId()))
                                    .toList();
                            return new ProductFeature(product.getProductId(), product.getName(), product.getQuantity(), product.getCost(),featureList);
                        }).doOnSuccess(result -> redisService.set(key,ProductFeature.class,3600L))
        );
    }

    private ProductAggregate createProductAggregate(
            SecurityContext sc,
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            List<Feature> features,
            String serviceAddress) {
        logAuthorizationInfo(sc);
        int productId = product.getProductId();
        String name = product.getName();
        int quantity = product.getQuantity();
        List<RecommendationSummary> recommendationSummaries =  (recommendations == null) ? null : recommendations.stream()
                .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate(),r.getContent()))
                .collect(Collectors.toList());
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null : reviews.stream()
                .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject(),r.getContent()))
                .collect(Collectors.toList());
        List<FeatureSummary> featureList = (reviews == null) ? null : features.stream()
                .map(r -> new FeatureSummary(r.getFeatureId(), r.getName(), r.getDescription(),r.getProductId()))
                .toList();
        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && !reviews.isEmpty()) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && !recommendations.isEmpty()) ? recommendations.get(0).getServiceAddress() : "";
        String featureAddress = (features != null && !features.isEmpty()) ?features.get(0).getServiceAddress() : "";
        ServiceAddress serviceAddresses = new ServiceAddress(serviceAddress, productAddress, reviewAddress, recommendationAddress,featureAddress);

        return new ProductAggregate(productId, name, quantity, product.getCost(),reviewSummaries, recommendationSummaries, featureList,serviceAddresses);
    }
    private Mono<SecurityContext> getLogAuthorizationInfoMono() {
        return getSecurityContextMono().doOnNext(this::logAuthorizationInfo);
    }

    private Mono<SecurityContext> getSecurityContextMono() {
        return ReactiveSecurityContextHolder.getContext().defaultIfEmpty(nullSecCtx);
    }

    private void logAuthorizationInfo(SecurityContext sc) {
        if (sc != null && sc.getAuthentication() != null && sc.getAuthentication() instanceof JwtAuthenticationToken) {
            Jwt jwtToken = ((JwtAuthenticationToken)sc.getAuthentication()).getToken();
            logAuthorizationInfo(jwtToken);
        } else {
            LOG.warn("No JWT based Authentication supplied, running tests are we?");
        }
    }

    private void logAuthorizationInfo(Jwt jwt) {
        if (jwt == null) {
            LOG.warn("No JWT supplied, running tests are we?");
        } else {
            if (LOG.isDebugEnabled()) {
                URL issuer = jwt.getIssuer();
                List<String> audience = jwt.getAudience();
                Object subject = jwt.getClaims().get("sub");
                Object scopes = jwt.getClaims().get("scope");
                Object expires = jwt.getClaims().get("exp");

                LOG.debug("Authorization info: Subject: {}, scopes: {}, expires {}: issuer: {}, audience: {}", subject, scopes, expires, issuer, audience);
            }
        }
    }
}