package com.example.Aiking.Controller;
import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Service.AuthServiceImplement;
import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Autowired
    private AuthServiceImplement authServiceImplement;
    @GetMapping("/test1")
    public void generateRole(){
        this.authServiceImplement.genaterRole();
    }
    @GetMapping("/test")
    public ResponseEntity<String> testNginx(){
        return new ResponseEntity<String>( "test auth user",HttpStatusCode.valueOf(200));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody AuthRequest req) {
        AuthResponse authResponse = authServiceImplement.login(req);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatusCode.valueOf(200));
    }
    @GetMapping("/oauth2/authorization/google")
    public String loginwithgoogle(){
        return "<div>\n" +
                "    <h2>Please Login</h2>\n" +
                "    <br/>\n" +
                "</div>\n" +
                "<div>\n" +
                "    <h4><a href=\"http://localhost:8080/oauth2/authorization/google\">Login with Google</a></h4>   \n" +
                "</div>\n"
                ;
    }


    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> regis (@RequestBody UserDTO req) {
        System.out.println(req.getPoint());
        AuthResponse authResponse = authServiceImplement.register(req);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatusCode.valueOf(200));
    }
}
