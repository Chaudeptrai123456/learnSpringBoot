package se.chau.microservices.util.http;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import javax.crypto.SecretKey;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Date;
@Component
public class JwtService implements ReactiveJwtDecoder {

    private static final ECKey ecKey =  Jwk.generateEc();

    public static String generateAccessToken(Authentication authentication) throws JOSEException {
//            ECKey ecKey = Jwk.generateEc();
        JWSSigner signer = new ECDSASigner(ecKey.toECPrivateKey());
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .keyID(ecKey.getKeyID()) // Use the key ID from ECKey
                .build();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject((String) authentication.getPrincipal())
                .issuer( authentication.getPrincipal().toString())
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .claim("authority", authentication.getAuthorities())
                .claim("username", authentication.getPrincipal())
                .build();
        SignedJWT signedJWT = new SignedJWT(header,claims);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
    public static String generateRefreshToken(Authentication authentication) throws JOSEException {
        JWSSigner signer = new ECDSASigner(ecKey.toECPrivateKey());
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                .keyID(ecKey.getKeyID()) // Use the key ID from ECKey
                .build();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject((String) authentication.getPrincipal())
                .issuer( authentication.getPrincipal().toString())
                .expirationTime(new Date(new Date().getTime() + 60 * 1000*365))
                .claim("authority", authentication.getAuthorities())
                .claim("username", authentication.getPrincipal())
                .build();
        SignedJWT signedJWT = new SignedJWT(header,claims);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
    public static Boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new ECDSAVerifier(ecKey.toECPublicKey());
            if (signedJWT.verify(verifier)) {
                // Optional: Further validation can be done here (like checking expiration)
                return true; // Token is valid
            }
        } catch (ParseException | JOSEException e) {
            e.printStackTrace(); // Handle parsing and verification exceptions
        }
        return false; // Token is invalid
    }
    public static String extractUsernameAccessToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getClaim("username").toString();
    }

    public static RSAKey generateRsa() {
        KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // @formatter:off
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        // @formatter:on
    }

    public static ECKey generateEc() {
        KeyPair keyPair = KeyGeneratorUtils.generateEcKey();
        ECPublicKey publicKey = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey privateKey = (ECPrivateKey) keyPair.getPrivate();
        Curve curve = Curve.forECParameterSpec(publicKey.getParams());
        // @formatter:off
        return new ECKey.Builder(curve, publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        // @formatter:on
    }

    public static OctetSequenceKey generateSecret() {
        SecretKey secretKey = KeyGeneratorUtils.generateSecretKey();
        // @formatter:off
        return new OctetSequenceKey.Builder(secretKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        // @formatter:on
    }
    public static String generateAccessTokenFromRefreshToken(String refreshToken) throws JOSEException, ParseException {
        if (validateToken(refreshToken)) {
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            String username = signedJWT.getJWTClaimsSet().getSubject();
            // Bạn có thể lấy thêm các thông tin khác từ claims nếu cần

            // Sử dụng thông tin từ Refresh Token để tạo Access Token mới
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer("YourIssuer") // Issuer có thể giữ nguyên hoặc thay đổi tùy vào yêu cầu
                    .expirationTime(new Date(new Date().getTime() + 60 * 1000)) // Ví dụ: Access Token có thời hạn 1 phút
                    .claim("authority", signedJWT.getJWTClaimsSet().getClaim("authority")) // Lấy authority từ Refresh Token
                    .claim("username", username)
                    .build();

            // Ký Access Token mới bằng private key
            JWSSigner signer = new ECDSASigner(ecKey.toECPrivateKey());
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .keyID(ecKey.getKeyID())
                    .build();

            SignedJWT newAccessToken = new SignedJWT(header, claims);
            newAccessToken.sign(signer);

            return newAccessToken.serialize();
        } else {
            throw new RuntimeException("Refresh token is invalid or expired.");
        }
    }

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (!validateToken(token)) {
                return Mono.error(new JwtException("Invalid JWT signature"));
            }
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return Mono.just(Jwt.withTokenValue(token)
                    .subject(claims.getSubject())
                    .claim("authority", claims.getClaim("authority"))
                    .claim("username", claims.getClaim("username"))
                    .expiresAt(claims.getExpirationTime().toInstant())
                    .build());
        } catch (ParseException e) {
            return Mono.error(new JwtException("Failed to decode JWT", e));
        }
    }


}
