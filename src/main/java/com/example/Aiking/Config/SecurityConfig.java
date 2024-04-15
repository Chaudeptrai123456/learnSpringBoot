package com.example.Aiking.Config;

import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Service.AuthServiceImplement;
import com.example.Aiking.Service.Jwt.JwtFilter;
import com.example.Aiking.Service.Oauth2.CustomOAuth2UserService;
import com.example.Aiking.Service.Oauth2.CustomOauthUser2;
import com.example.Aiking.Service.User.Oauth2UserService;
import com.example.Aiking.Service.User.UserDetailsService;
import com.example.Aiking.Service.User.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private Oauth2UserService oauth2UserService;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/auth/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(req->{
                    req.loginPage("/oauth2/authorization/google");
                    req.userInfoEndpoint(userInfoEndpointConfig -> {
                        userInfoEndpointConfig.userService(customOAuth2UserService);
                    });
                    req.successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            CustomOauthUser2 customOauthUser2 = (CustomOauthUser2) authentication.getPrincipal();
                            UserDTO userDTO = new UserDTO();
                            userDTO.setEmail(customOauthUser2.getEmail());
                            userDTO.setUserName(customOauthUser2.getName());
                            userDTO.setPoint(1);
                            userDTO.setPassword(passwordEncoder().encode("123"));
                            oauth2UserService.handleResgiter(userDTO);
                            System.out.println(authentication.toString());
                            System.out.println("test "+ customOauthUser2.getEmail());
                            response.sendRedirect("/auth/test/oauth");
                        }
                    });
                })
                .authenticationProvider(auhtenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        ;
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider auhtenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
