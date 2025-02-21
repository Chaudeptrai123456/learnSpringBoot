//package se.chau.microservices.core.recommendation.Config;
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
//public class Security {
//
//    private SecurityWebFiltersOrder JwtAuthenticationToken;
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .authorizeExchange()
//                .pathMatchers("/actuator/**").permitAll()
//                .pathMatchers("/public/**").permitAll()
//                .pathMatchers(POST, "/recommendation/**").authenticated()
//                .pathMatchers(DELETE, "/recommendation/**").authenticated()
//                .pathMatchers(GET, "/recommendation/**").authenticated()
//                .anyExchange().authenticated()
//                .and()
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
//        return http.build();
//    }
//}