package se.chau.microservices.api.core.order;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    @PostMapping(
            value="/order/make"
    )
    Mono<Order> makingOrder(@RequestHeader("Authorization")  String token, @RequestBody Order body);
    @GetMapping(
            value="/order/user/{userId}"
    )
    Flux<Order> findOrderByUserId(@PathVariable int userId);
    @GetMapping(
            value="/order/confirm"
    )
    Flux<Order> confirmOrder(@RequestBody int orderId);
    @GetMapping(
            value="/order/confirm{orderId}"
    )
    Flux<Order> getOrderInfo(@PathVariable int orderId);

}
