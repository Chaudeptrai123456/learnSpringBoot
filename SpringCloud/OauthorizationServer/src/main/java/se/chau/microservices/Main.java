package se.chau.microservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@ComponentScan("se.chau")
public class Main {
//$ curl -k -u writer:secret-writer https://localhost:8443/oauth2/token -d "grant_type=client_credentials"  -d "scope=product:read product:write"
   //https://localhost:8443/oauth2/token?grant_type=client_credentials&scope=product:read%20product:write
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}