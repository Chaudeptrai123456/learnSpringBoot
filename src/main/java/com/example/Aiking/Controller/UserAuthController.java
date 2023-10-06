package com.example.Aiking.Controller;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Service.AuthServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Autowired
    private AuthServiceImplement authServiceImplement;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody AuthRequest req) {
        AuthResponse authResponse = authServiceImplement.login(req);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> regis (@RequestBody UserDTO req) {
        System.out.println(req.getPoint());
        AuthResponse authResponse = authServiceImplement.register(req);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatusCode.valueOf(200));
    }


}
