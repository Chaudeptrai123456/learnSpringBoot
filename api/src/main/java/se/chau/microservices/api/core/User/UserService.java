package se.chau.microservices.api.core.User;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserService {
    @PostMapping(
            value    = "/oauth2/user/register",
            produces = "application/json"
    )
    ResponseEntity<String>  Register(@RequestBody  User user);
    @PostMapping(
            value    = "/user/login",
            produces = "application/json"
    )
    ResponseEntity<Token> Login(@RequestBody Account account);
    @GetMapping(
            value="/user/test",
            produces = "application/json"

    )
    ResponseEntity<String> testSecurity();

    @PostMapping(
            value = "/user/getAccess",
            produces = "application/json"
    )
    ResponseEntity<String> getAccessToken(HttpServletRequest request)  ;
    @GetMapping(
            value = "/oauth2/user/verify"
    )
    ResponseEntity<String> verifyRegister(@RequestParam String email, @RequestParam String otp);

}
