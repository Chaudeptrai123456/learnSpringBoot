package se.chau.microservices.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("se.chau.microservices")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
}