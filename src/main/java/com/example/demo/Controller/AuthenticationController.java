package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.SignInRequest;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Service.UserServiceImplement;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	UserServiceImplement userService;
	 @PostMapping("/signin")
	  public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest request) {
		 	LoginResponse res = userService.handleLogin(request);
		 	return new ResponseEntity<LoginResponse>(res,HttpStatus.ACCEPTED);
	  }
	 @GetMapping("/test")
	 public ResponseEntity<?> test(){
		 return ResponseEntity.ok("test");
	 }
	  @PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Validated @RequestBody SignInRequest request) {
		LoginResponse res = userService.handleSignup(request);
	    return new ResponseEntity<LoginResponse>(res,HttpStatus.ACCEPTED);
		    //return new ResponseEntity<String>(request.getRole(),HttpStatus.ACCEPTED);
	  }
}
