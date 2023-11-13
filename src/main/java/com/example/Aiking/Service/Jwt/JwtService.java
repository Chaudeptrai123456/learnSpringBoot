package com.example.Aiking.Service.Jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import java.security.Key;
import java.util.Date;
import java.util.logging.Level;

@Component
public class JwtService {
    private static final Logger logger =  LoggerFactory.getLogger(JwtService.class);

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    private long jwtExpirationDate = 3600*1000*1212L;
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // get username from Jwt token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // validate Jwt token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error( "Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error( "JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error( "JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error( "JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
