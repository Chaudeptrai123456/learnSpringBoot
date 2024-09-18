package se.chau.microservices.core.recommendation.Persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface    RecommendationRepository extends ReactiveCrudRepository<RecommendationEntity,String> {

    Flux<RecommendationEntity> findByProductId(int productId);
}
