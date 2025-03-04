package se.chau.microservices.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }
    @RestController
    static class CustomErrorController implements ErrorController {

        private static final String ERROR_MAPPING = "/error";

        @RequestMapping(ERROR_MAPPING)
        public ResponseEntity<Void> error() {
            return ResponseEntity.notFound().build();
        }
    }
}