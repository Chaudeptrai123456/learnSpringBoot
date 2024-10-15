    package se.chau.microservices.authorization.jwt;

    import com.nimbusds.jose.*;
    import com.nimbusds.jose.crypto.ECDSASigner;
    import com.nimbusds.jose.crypto.ECDSAVerifier;
    import com.nimbusds.jose.jwk.ECKey;
    import com.nimbusds.jwt.JWTClaimsSet;
    import com.nimbusds.jwt.SignedJWT;
    import org.springframework.security.core.Authentication;
    import org.springframework.stereotype.Component;

    import java.text.ParseException;
    import java.util.Date;
    @Component
    public class JwtService {

        private static final ECKey ecKey =  Jwk.generateEc();

        public static String generateAccessToken(Authentication authentication) throws JOSEException {
//            ECKey ecKey = Jwk.generateEc();
            JWSSigner signer = new ECDSASigner(ecKey.toECPrivateKey());
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .keyID(ecKey.getKeyID()) // Use the key ID from ECKey
                    .build();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject((String) authentication.getPrincipal())
                    .issuer("test")
                    .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                    .claim("authority", authentication.getAuthorities())
                    .build();
            SignedJWT signedJWT = new SignedJWT(header,claims);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        }
        public static String generateRefreshToken(Authentication authentication) throws JOSEException {
            JWSSigner signer = new ECDSASigner(ecKey.toECPrivateKey());
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
                    .keyID(ecKey.getKeyID())  // Sử dụng key ID từ ECKey
                    .build();
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject((String) authentication.getPrincipal())
                    .issuer("test")
                    .expirationTime(new Date(new Date().getTime() + 60 * 1000*365))
                    .claim("authority", authentication.getAuthorities())
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
    }
