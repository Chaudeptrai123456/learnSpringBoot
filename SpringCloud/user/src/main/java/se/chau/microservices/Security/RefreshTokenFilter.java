package se.chau.microservices.Security;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se.chau.microservices.Service.JpaUserDetailsService;
import se.chau.microservices.jwt.JwtService;

import java.io.IOException;
import java.text.ParseException;

@Component
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final JpaUserDetailsService userDetailsService;
    @Autowired
    public RefreshTokenFilter(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization"); // Get authorization header
        final String jwt;
        final String userEmail;
        // If authorization header is null or does not start with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Refresh ")) {
            filterChain.doFilter(request, response); // Continue the filter chain
            return;
        }
        jwt = authorizationHeader.substring(8); // Get JWT
        try {
            userEmail = JwtService.extractUsernameAccessToken(jwt);
            // Extract user email from JWT token;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test1 " + userEmail != null || SecurityContextHolder.getContext().getAuthentication() == null);

            if (userEmail != null || SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load user details from database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                // If JWT token is valid
                System.out.println("test 2 " + JwtService.validateToken(jwt));

                if (JwtService.validateToken(jwt)) {

                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    // Set authentication token details
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    // Update SecurityContext using authentication token
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    try {
                        String a = JwtService.generateAccessToken(authToken);
                        System.out.println(a.toString());
                    } catch (JOSEException e) {
                        throw new RuntimeException(e);
                    }
                }
        }
        filterChain.doFilter(request, response);

    }
}
