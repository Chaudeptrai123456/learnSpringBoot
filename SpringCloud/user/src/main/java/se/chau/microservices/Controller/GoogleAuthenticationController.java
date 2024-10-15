package se.chau.microservices.Controller;

import ch.qos.logback.core.model.Model;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import se.chau.microservices.api.core.User.GoogleService;

@RestController
public class GoogleAuthenticationController implements GoogleService {
    @Override
    public String loginGoogle() {
        System.out.println("test google");
        return "login";
    }

    @Override
    public String home(OAuth2User principal, Model model) {
        return null;
    }
}
