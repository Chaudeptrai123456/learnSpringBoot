package se.chau.microservices;

import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
@EnableConfigServer
@Configuration
public class Security {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
            http
                    // Disable CRCF to allow POST to /encrypt and /decrypt endpoins
                    .csrf()
                    .disable()
                    .authorizeHttpRequests(req->req.anyRequest().permitAll());
            return http.build();
    }
}
