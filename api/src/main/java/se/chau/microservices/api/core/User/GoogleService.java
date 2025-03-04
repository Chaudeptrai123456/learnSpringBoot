package se.chau.microservices.api.core.User;

import ch.qos.logback.core.model.Model;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;

public interface GoogleService {
    @GetMapping(
            value = "/test",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> loginGoogle()throws Exception;

    @GetMapping(
            value = "/user/home/google",
            produces = "application/json"
    )
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model);
}
