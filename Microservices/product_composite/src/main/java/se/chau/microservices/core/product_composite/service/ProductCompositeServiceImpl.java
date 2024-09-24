package se.chau.microservices.core.product_composite.service;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.composite.product.*;
import se.chau.microservices.api.core.Feature.Feature;
import se.chau.microservices.api.core.Feature.FeatureForSearchPro;
import se.chau.microservices.api.core.product.Product;
import se.chau.microservices.api.core.recommandation.Recommendation;
import se.chau.microservices.api.core.review.Review;
import se.chau.microservices.util.http.ServiceUtil;

import static java.util.logging.Level.FINE;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;
    @Autowired
    public ProductCompositeServiceImpl(ServiceUtil serviceUtil, ProductCompositeIntegration integration ) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }
    @Override
    public Mono<Void> createProduct(ProductAggregate body) {
        try {

            List<Mono> monoList = new ArrayList<>();

            LOG.info("Will create a new composite entity for product.id: {}", body.getProductId());

            Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
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

    @Override
    public Mono<ProductAggregate> getProduct(int productId) {
        return  Mono.zip(
                        values -> createProductAggregate(
                                (Product) values[0],
                                (List<Recommendation>) values[1],
                                (List<Review>) values[2],
                                (List<Feature>) values[3],
                                serviceUtil.getServiceAddress()),
                        integration.getProduct(productId),
                        integration.getRecommendations(productId).collectList(),
                        integration.getReviews(productId).collectList(),
                        integration.getFeatureOfProduct(productId).collectList())
                .doOnError(ex ->
                        LOG.warn("getCompositeProduct failed: {}",
                                ex.toString()))
                .log(LOG.getName(), FINE);

    }

    @Override
    public Set<Product> getProductByFeature(@RequestBody List<FeatureForSearchPro> feature) throws EnumConstantNotPresentException {
        Set<Product> result = new HashSet<>();
        feature.forEach(index->{
            Objects.requireNonNull(integration.getProductByFeature(index).collectList().block()).forEach(r->{
                result.add(integration.getProduct(r.getProductId()).block());
            });
        });

        return result;
    }

    @Override
    public void deleteProduct(int productId) {
        integration.deleteProduct(productId);
    }

    private ProductAggregate createProductAggregate(
            Product product,
            List<Recommendation> recommendations,
            List<Review> reviews,
            List<Feature> features,
            String serviceAddress) {
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();
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

        return new ProductAggregate(productId, name, weight, reviewSummaries, recommendationSummaries, featureList,serviceAddresses);
    }
}