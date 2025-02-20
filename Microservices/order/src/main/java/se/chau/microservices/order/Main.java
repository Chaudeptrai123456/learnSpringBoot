package se.chau.microservices.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication(scanBasePackages = {"se.chau", "se.chau.microservices.util.http"})
@ComponentScan("se.chau")
public class Main {
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}