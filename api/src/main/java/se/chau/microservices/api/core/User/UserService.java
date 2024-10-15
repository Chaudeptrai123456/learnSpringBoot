package se.chau.microservices.api.core.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.text.ParseException;

public interface UserService {
    @PostMapping(
            value    = "/user/register",
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
            consumes = "application/json",
            produces = "application/json"

    )
    ResponseEntity<String> testSecurity();

    @PostMapping(
            value = "/user/getAccess",
            produces = "application/json"
    )
    ResponseEntity<String> getAccessToken(HttpServletRequest request)  ;

}
