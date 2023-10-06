package com.example.Aiking.Controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping()
    public ResponseEntity<String> a() {
        return new ResponseEntity<>("test auht", HttpStatusCode.valueOf(200));
    }
}
