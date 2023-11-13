package com.example.Aiking.Controller;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Service.Auth.AuthServiceImplement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> regis (@RequestBody UserDTO req) {
        User authResponse = authServiceImplement.register(req);
        return new ResponseEntity<User>(authResponse, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> handleLogout(HttpServletRequest req) throws ServletException {
        req.logout();
        return new ResponseEntity<String>("log out",HttpStatusCode.valueOf(200));
    }
}