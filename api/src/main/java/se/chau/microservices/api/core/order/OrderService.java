package se.chau.microservices.api.core.order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public interface OrderService {
    @PostMapping(
            value="/order/make"
    )
    Mono<Order> makingOrder(@RequestBody Order body);

}
