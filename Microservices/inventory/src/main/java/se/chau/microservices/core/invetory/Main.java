package se.chau.microservices.core.invetory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "se.chau.microservices.core.inventory")
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }
}