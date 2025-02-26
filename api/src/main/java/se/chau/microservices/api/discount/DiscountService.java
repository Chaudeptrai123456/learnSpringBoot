package se.chau.microservices.api.discount;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DiscountService {
    @PostMapping(
            value = "/discount/create",
            produces =  "application/json",
            consumes = "application/json"
    )
    Mono<Discount> createDiscount(@RequestBody Discount discount);
    @GetMapping(
            value = "/discount/product/{productId}"
    )
    Flux<Discount> getDiscountOfPro(@PathVariable int productId);
}
