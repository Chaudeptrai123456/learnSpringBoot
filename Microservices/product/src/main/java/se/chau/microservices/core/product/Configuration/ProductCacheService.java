package se.chau.microservices.core.product.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.product.Product;

import java.time.Duration;

@Component
public class ProductCacheService {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCacheService.class);

    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, Product> productOps;


    public ProductCacheService(ReactiveRedisConnectionFactory factory,@Qualifier("redisOperations") ReactiveRedisOperations<String, Product> productOps) {
        this.factory = factory;
        this.productOps = productOps;
    }
    public Mono<Product> writeProductToRedis(Product product) {
        return productOps.opsForValue()
                .set(String.valueOf(product.getProductId()), product, Duration.ofHours(2))  // Write to Redis
                .thenReturn(product);  // Return the Product after it's written to Redis
    }

    public Mono<Boolean> checkProductInCache(String productId) {
        return productOps.opsForValue()
                .get(productId)
                .map(product -> true) // If product is found, return true
                .defaultIfEmpty(false); // If product is not found, return false
    }
    public Mono<Product> getProductFromCache(String productId) {
        LOG.info("Redis get product id : " + productId);
        return productOps.opsForValue()
                .get(productId).cache(Duration.ofDays(1)); // Retrieves the product associated with the productId from Redis
    }
}
