package se.chau.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Main {
//$ curl -k -u writer:secret-writer https://localhost:8443/oauth2/token -d "grant_type=client_credentials"  -d "scope=product:read product:write"
   //https://localhost:8443/oauth2/token?grant_type=client_credentials&scope=product:read%20product:write

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}