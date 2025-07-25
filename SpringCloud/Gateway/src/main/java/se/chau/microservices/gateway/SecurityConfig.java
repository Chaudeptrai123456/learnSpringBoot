package se.chau.microservices.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges

                        // Public routes
                        .pathMatchers(
                                "/header/routing/**",
                                "/oauth2/user/register/**",
                                "/actuator/**",
                                "/eureka/**",
                                "/oauth2/**",
                                "/login/**",
                                "/error/**",
                                "/openapi/**",
                                "/webjars/**",
                                "/config/**"
                        ).permitAll()

                        // Order service (needs USER or ADMIN)
                        .pathMatchers(HttpMethod.GET, "/order/**").hasAnyAuthority("USER", "ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/order/**").hasAnyAuthority("USER", "ADMIN")
                        .pathMatchers(HttpMethod.POST, "/order/**").hasAnyAuthority("USER", "ADMIN")

                        // Product-composite service
                        .pathMatchers(HttpMethod.GET, "/product-composite/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/product-composite/**").hasAuthority("ADMIN")

                        // Other microservices (GET public, write restricted)
                        .pathMatchers(HttpMethod.GET,
                                "/product/**", "/review/**", "/recommendation/**", "/discount/**").permitAll()
                        .pathMatchers(HttpMethod.POST,
                                "/product/**", "/review/**", "/recommendation/**", "/discount/**").hasAuthority("ADMIN")
                        .pathMatchers(HttpMethod.PATCH,
                                "/product/**", "/review/**", "/recommendation/**", "/discount/**").hasAuthority("ADMIN")

                        // Any other POST requires USER or ADMIN
                        .pathMatchers(HttpMethod.POST).hasAnyAuthority("USER", "ADMIN")

                        // Any other request requires authentication
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                )
                .build();
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor());
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    static class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<?> roles = (Collection<?>) jwt.getClaims().getOrDefault("roles", Collections.emptyList());
            return roles.stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
}
