package se.chau.microservices.core.product.Persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ImageRepository extends ReactiveCrudRepository<ImageEntity,String> {

    Flux<ImageEntity> findByProductId(int productId);

}
