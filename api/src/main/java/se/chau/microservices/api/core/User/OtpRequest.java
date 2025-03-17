package se.chau.microservices.api.core.User;

public class OtpRequest {
    private String email;
    private String username;
    private String password;
    private String otp;

    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getOtp() { return otp; }
}
