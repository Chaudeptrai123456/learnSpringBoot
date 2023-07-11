package com.example.demo.Service.Jwt;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

 import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.example.demo.Service.*;

@Component
public class JwtService {
 
  @Value("${jwt.Secret}")
  private final String jwtSecret = "asflksjdafjskldfjslkajdflksadjflksdjflksdjfklsdjflkdsjfklsdjfkl";

  @Value("${jwt.Expire}")
  private final int jwtExpirationMs = 360000000;

  public String generateJwtToken(Authentication authentication) {

	 UserDetailService userPrincipal = (UserDetailService) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
  
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      System.out.printf("Invalid JWT token: {}", e.getMessage().toString());
    } catch (ExpiredJwtException e) {
    	System.out.printf("Invalid JWT token: {}", e.getMessage().toString());
    } catch (UnsupportedJwtException e) {
    	System.out.printf("Invalid JWT token: {}", e.getMessage().toString());
    } catch (IllegalArgumentException e) {
    	System.out.printf("Invalid JWT token: {}", e.getMessage().toString());
    }

    return false;
  }
}
