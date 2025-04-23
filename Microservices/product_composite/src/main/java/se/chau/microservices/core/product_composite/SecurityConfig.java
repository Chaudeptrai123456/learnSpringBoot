package se.chau.microservices.core.product_composite;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@EnableWebFluxSecurity // Sử dụng @ServerHttpSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(auth->{
                    auth .pathMatchers("/actuator/**").permitAll()
                            .pathMatchers("/public/**").permitAll()
                            .pathMatchers(HttpMethod.POST, "/product-composite/**").authenticated() // Sử dụng HttpMethod
                            .pathMatchers(HttpMethod.DELETE, "/product-composite/**").authenticated() // Sử dụng HttpMethod
                            .pathMatchers(HttpMethod.GET, "/product-composite/**").authenticated() // Sử dụng HttpMethod
                            .anyExchange().authenticated();
                })
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(jwtSpec -> {
                }))
               ; // Thêm dòng này để tắt CSRF nếu cần
        return http.build();
    }
}
