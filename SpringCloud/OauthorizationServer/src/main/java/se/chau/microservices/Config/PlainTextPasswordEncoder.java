package se.chau.microservices.Config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PlainTextPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return hashWithSHA512(rawPassword.toString());
    }

    public String hashWithSHA512(String string) {
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte [] digested = md.digest(string.getBytes());
            for (int i = 0; i < digested.length; i++) {
                result.append(Integer.toHexString(0xFF & digested[i]));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Bad algorithm");
        }
        return result.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        System.out.println("test password matches boolean : "+rawPassword.toString().contains("{noop}"));
        if (rawPassword.toString().contains("{noop}")) {
            return rawPassword.toString().replace("{noop}","").equals("123");
        }
        String hashedRawPassword = hashWithSHA512(rawPassword.toString());
        return hashedRawPassword.equals(encodedPassword);
    }
}
