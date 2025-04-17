package se.chau.microservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;
import se.chau.microservices.Entity.Authority;
import se.chau.microservices.Service.AuthorityRepository;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@ComponentScan("se.chau")
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    CommandLineRunner initAuthorities(AuthorityRepository authorityRepository) {
        return args -> {
            List<String> roles = Arrays.asList("USER", "ADMIN");

            for (String role : roles) {
                if (!authorityRepository.existsByName(role)) {
                    Authority authority = new Authority();
                    authority.setName(role);
                    authorityRepository.save(authority);
                    LOG.info("✅ Created role: " + role);
                } else {
                    LOG.info("⚡ Role already exists: " + role);
                }
            }
        };
    }
}
