package se.chau.microservices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Security {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                // Disable CRCF to allow POST to /encrypt and /decrypt endpoins
                .csrf(csrf ->csrf.disable())
                .authorizeRequests(req->{
                    req.anyRequest().authenticated();
                })
                .httpBasic();
        return http.build();
    }
}
