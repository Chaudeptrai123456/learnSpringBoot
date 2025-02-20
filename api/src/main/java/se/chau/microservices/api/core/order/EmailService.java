package se.chau.microservices.api.core.order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface EmailService {
    @PostMapping(
            value = "/email/confirmOrder"
    )
    void placeOrder(@RequestBody Email email);

}
