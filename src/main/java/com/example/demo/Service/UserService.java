package com.example.demo.Service;

 import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.SignInRequest;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.User;

public interface UserService {

	public User findUserByEmail(String email);
	public User saveUser(UserDTO userDTO);
	public void addRoletoUser(String email,String roleName);
	public LoginResponse handleLogin(LoginRequest req);
	public LoginResponse handleSignup(SignInRequest req);
}
