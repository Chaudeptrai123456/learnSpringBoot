package se.chau.microservices.api.core.order;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    @PostMapping(
            value="/order/make"
    )
    Mono<Order> makingOrder(@RequestBody Order body);
    @PostMapping(
            value="/order/user/{userId}"
    )
    Flux<Order> findOrderByUserId(@PathVariable int userId);
    @PostMapping(
            value="/order/confirm"
    )
    Flux<Order> confirmOrder(@RequestBody int orderId);

}
