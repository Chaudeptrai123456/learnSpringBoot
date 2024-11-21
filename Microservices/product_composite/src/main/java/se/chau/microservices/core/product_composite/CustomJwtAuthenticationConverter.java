package se.chau.microservices.core.product_composite;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        System.out.println("JWT Claims: " + jwt.getClaims());
        System.out.println("JWT Subject: " + jwt.getSubject());
        return new JwtAuthenticationToken(jwt);
    }
}