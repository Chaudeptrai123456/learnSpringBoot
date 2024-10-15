package se.chau.microservices;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("se.chau")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}