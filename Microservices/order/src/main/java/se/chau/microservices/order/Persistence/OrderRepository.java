package se.chau.microservices.order.Persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository  extends ReactiveCrudRepository<OrderEntity,String> {

    Flux<OrderEntity> findByUserId(int userId);

    Flux<OrderEntity> findByOrderDate(int userId);

    Flux<OrderEntity> findByOrderId(int orderId);
}