package se.chau.microservices.Config;

public interface PasswordEncode  {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword,String encodePassword);
}
