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
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec
                            .pathMatchers("/header/routing/**").permitAll()
                            .pathMatchers(HttpMethod.POST,"/oauth2/user/register/**").permitAll()

                            .pathMatchers("/actuator/**").permitAll()
                            .pathMatchers("/eureka/**").permitAll()
                            .pathMatchers("/oauth2/**").permitAll()
                            .pathMatchers("/login/**").permitAll()
                            .pathMatchers("/error/**").permitAll()
                            .pathMatchers("/openapi/**").permitAll()
                            .pathMatchers("/webjars/**").permitAll()
                            .pathMatchers("/config/**").permitAll()

                            // order service (chỉ USER, ADMIN mới được phép tạo order)
                            .pathMatchers(HttpMethod.GET, "/order/**").hasAnyAuthority("USER", "ADMIN")
                            .pathMatchers(HttpMethod.PATCH, "/order/**").hasAnyAuthority("USER", "ADMIN")
                            .pathMatchers(HttpMethod.POST, "/order/**").hasAnyAuthority("USER", "ADMIN")

                            // product-composite service (GET không cần login, nhưng POST cần ADMIN)
                            .pathMatchers(HttpMethod.GET, "/product-composite/**").permitAll()
                            .pathMatchers(HttpMethod.POST, "/product-composite/**").hasAnyAuthority("ADMIN")

                            // other services (GET không cần login, POST/PATCH cần ADMIN)
                            .pathMatchers(HttpMethod.GET, "/product/**", "/review/**", "/recommendation/**", "/discount/**").permitAll()
                            .pathMatchers(HttpMethod.POST, "/product/**", "/review/**", "/recommendation/**", "/discount/**").hasAnyAuthority("ADMIN")
                            .pathMatchers(HttpMethod.PATCH, "/product/**", "/review/**", "/recommendation/**", "/discount/**").hasAnyAuthority("ADMIN")

                            .pathMatchers(HttpMethod.POST).hasAnyAuthority("USER","ADMIN")
                            .anyExchange().authenticated()
                            .and()
                            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
                });

        return http.build();
    }
    Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =  new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
    static class GrantedAuthoritiesExtractor  implements Converter<Jwt, Collection<GrantedAuthority>> {

        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Collection<?> authorities = (Collection<?>)
                    jwt.getClaims().getOrDefault("roles", Collections.emptyList());
            return authorities.stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }
//    @Bean
//    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
//        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(authorizeExchangeSpec -> {
//                        authorizeExchangeSpec  .pathMatchers("/headerrouting/**").permitAll()
//                                .pathMatchers("/actuator/**").permitAll()
//                                .pathMatchers("/eureka/**").permitAll()
//                                .pathMatchers("/oauth2/**").permitAll()
//                                .pathMatchers("/login/**").permitAll()
//                                .pathMatchers("/error/**").permitAll()
//                                .pathMatchers("/openapi/**").permitAll()
//                                .pathMatchers("/webjars/**").permitAll()
//                                .pathMatchers("/config/**").permitAll()
//                                .pathMatchers(HttpMethod.GET).hasAnyAuthority("USER","ADMIN")
//                                .anyExchange().authenticated().and()
//                                .oauth2ResourceServer()
//                                .jwt();
//                });
//
//        return http.build();
//    }


}