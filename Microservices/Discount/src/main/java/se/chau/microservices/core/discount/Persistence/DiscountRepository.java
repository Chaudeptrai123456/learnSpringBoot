package se.chau.microservices.core.discount.Persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends ReactiveCrudRepository<DiscountEntity,Integer> {

    Flux<DiscountEntity> findByProductId(int productId);

    Flux<DiscountEntity> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate now);

    List<DiscountEntity> findByEndDateBefore(LocalDate now);

}
