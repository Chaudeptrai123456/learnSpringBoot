package se.chau.microservices.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"se.chau", "se.chau.microservices.util.http"})
@ComponentScan("se.chau")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}