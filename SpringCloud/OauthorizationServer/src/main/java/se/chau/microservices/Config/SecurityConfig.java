package se.chau.microservices.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import se.chau.microservices.Service.UserAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http    .csrf(csrf -> csrf.ignoringRequestMatchers("/oauth2/user/register"))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/actuator/**","/login.css","/user/login","/user/register","/oauth2/user/register").permitAll()
                        .requestMatchers("/oauth2/user/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product-composite/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/product/**", "/review/**", "/recommendation/**", "/discount/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(Customizer.withDefaults());

                .formLogin(form -> form.loginPage("/login").permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        ProviderManager providerManager = new ProviderManager(userAuthenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }


}