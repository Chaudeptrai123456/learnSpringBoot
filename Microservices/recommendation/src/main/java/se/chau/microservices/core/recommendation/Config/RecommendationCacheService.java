package se.chau.microservices.core.recommendation.Config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.recommandation.Recommendation;

import java.time.Duration;

@Component
public class RecommendationCacheService {
    private static final Logger LOG = LoggerFactory.getLogger(RecommendationCacheService.class);

    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, Recommendation> redisOperations;


    public RecommendationCacheService(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Recommendation> redisOperations) {
        this.factory = factory;
        this.redisOperations = redisOperations;
    }
    public Mono<Recommendation> writeRecommendationToRedis(Recommendation recommendation) {
        return redisOperations.opsForValue()
                .set(String.valueOf(recommendation.getProductId()), recommendation)  // Write to Redis
                .thenReturn(recommendation);  // Return the Product after it's written to Redis
    }

    public Mono<Boolean> checkRecommendationInCache(String productId) {
        return redisOperations.opsForValue()
                .get(productId)
                .map(product -> true) // If product is found, return true
                .defaultIfEmpty(false); // If product is not found, return false
    }
    public Mono<Recommendation> getRecommendationFromCache(String productId) {
        LOG.info("Redis get product id : " + productId);
        return redisOperations.opsForValue()
                .get(productId).cache(Duration.ofDays(1)); // Retrieves the product associated with the productId from Redis
    }

}
