//package se.chau.microservices.core.product.Configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//
//import static org.springframework.http.HttpMethod.*;
//
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    private SecurityWebFiltersOrder JwtAuthenticationToken;
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange()
//                .pathMatchers("/actuator/**").permitAll()
//                .pathMatchers("/public/**").permitAll()
//                .pathMatchers(GET, "/product/**").permitAll()
//                .pathMatchers(POST, "/product/**").authenticated()
//                .pathMatchers(DELETE, "/product/**").authenticated()
////                .anyExchange().authenticated()
//                .and()
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
//        return http.build();
//    }
//}
