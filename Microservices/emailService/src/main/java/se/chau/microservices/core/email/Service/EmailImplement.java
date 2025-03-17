package se.chau.microservices.core.email.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import se.chau.microservices.api.core.User.User;
import se.chau.microservices.api.core.order.DiscountEmail;
import se.chau.microservices.api.core.order.Email;

@Controller
public class EmailImplement implements se.chau.microservices.api.core.order.EmailService {
    private final EmailService emailService;
    @Autowired
    public EmailImplement(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void placeOrder(Email email) {
        System.out.println(email.getOrderId());
        this.emailService.sendEmail(email);
    }

    @Override
    public void sendOtp(User request) {
        this.emailService.sendOpt(request);
    }

    public void notificationDiscount(DiscountEmail email) {
        this.emailService.notificationDiscount(email);
    }
}
