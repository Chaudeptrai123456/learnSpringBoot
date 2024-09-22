package se.chau.microservices.core.feature.Persistence;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import se.chau.microservices.api.core.Feature.Feature;
import se.chau.microservices.api.core.product.Product;
@Repository
public interface FeatureRepository extends ReactiveCrudRepository<FeatureEntity,Integer> {
    Flux<FeatureEntity> findByProductId(int productId);

    Flux<FeatureEntity> findByNameAndDescription(String name,String description);
}
