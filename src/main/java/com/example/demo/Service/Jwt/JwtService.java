package com.example.demo.Service.Jwt;
import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.example.demo.Entity.*;
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

  public String generateTokenSignup(User user) {
	  return Jwts.builder()
		        .setSubject(user.getusername())
		       // .setSubject(String.format("%s,%s",userPrincipal.getId(),userPrincipal.getEmail() ))
		        .setIssuer("Authenticate User")
		    	.setIssuedAt(new Date())
		    	.claim("roles", user.getRole())
		    	.claim("info", String.format("%s,%s",user.getId(),user.getusername()) )
		        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
		        .signWith(key(), SignatureAlgorithm.HS256)
		        .compact();
	  }
  
  public String generateJwtToken(Authentication authentication) {

	 UserDetailService userPrincipal = (UserDetailService) authentication.getPrincipal();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
       // .setSubject(String.format("%s,%s",userPrincipal.getId(),userPrincipal.getEmail() ))
        .setIssuer("Authenticate User")
    	.setIssuedAt(new Date())
    	.claim("roles", userPrincipal.getAuthorities().toString())
    	//.claim("info", String.format("%s,%s",userPrincipal.getId(),userPrincipal.getEmail() ))
    	.claim("info", String.format("%s,%s",userPrincipal.getId(),userPrincipal.getUsername() ))
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
  public String getUserDetailFromToken(String token) {
//	 User user = new User();
	 Claims claim =  Jwts.parserBuilder().setSigningKey(key()).build()
             .parseClaimsJws(token).getBody();
	 String role =   claim.get("roles").toString();
	 String info =  claim.get("info").toString();
//	 role = role.replace("[", "").replace("]", "").replace("authority=", "").replace("{", "").replace("}", "");
// 	 String[] roleNames = role.split(",");
//	 for (String aRoleName : roleNames) {
//		 Role role1 = new Role();
//		 role1.setRoleName(aRoleName);
//		 user.addRole(role1);
//	 }	
	 String[] jwtSubject = info.split(",");
 //	 user.setId(Long.parseLong(jwtSubject[0]));
//	 user.setusername(jwtSubject[1]);
	 return jwtSubject[1];
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
