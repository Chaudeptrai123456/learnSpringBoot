//package se.chau.microservices.core.product_composite.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.Customizer;
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
//        http.authorizeExchange( authorizeExchangeSpec -> {
//                    authorizeExchangeSpec                .pathMatchers("/actuator/**").permitAll()
//                            .pathMatchers("/public/**").permitAll()
//                            .pathMatchers(GET, "/product-composite/**").permitAll()
//                            .pathMatchers(POST, "/product-composite/**").authenticated()
//                            .pathMatchers(DELETE, "/product-composite/**").authenticated()
//                            .anyExchange().authenticated();
//                })
//            .oauth2ResourceServer(oauth2 ->
//                    oauth2.jwt(Customizer.withDefaults()
//                ));
//        return http.build();
//    }
//}
