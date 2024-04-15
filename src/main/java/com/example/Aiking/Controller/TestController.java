package com.example.Aiking.Controller;

import com.nimbusds.oauth2.sdk.Request;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> a() {
        return new ResponseEntity<>("test auht", HttpStatusCode.valueOf(200));
    }
    @GetMapping("/auth/test/oauth")
    public ResponseEntity<String> n(Request request) {
        return new ResponseEntity<>("test google auth", HttpStatusCode.valueOf(200));
    }
}
