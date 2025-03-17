package se.chau.microservices.api.core.order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.chau.microservices.api.core.User.User;

public interface EmailService {
    @PostMapping(
            value = "/email/confirmOrder"
    )
    void placeOrder(@RequestBody Email email);
    @PostMapping(
            value="/oauth2/user/register/opt"
    )
    void sendOtp(@RequestBody User otpRequest);

}
