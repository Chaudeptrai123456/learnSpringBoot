package se.chau.microservices.core.feature.Persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FeatureRepository extends ReactiveCrudRepository<FeatureEntity,Integer> {
    Flux<FeatureEntity> findByProductId(int productId);

    Flux<FeatureEntity> findByNameAndDescription(String name,String description);

    Mono<FeatureEntity> findByName(String name);
}
