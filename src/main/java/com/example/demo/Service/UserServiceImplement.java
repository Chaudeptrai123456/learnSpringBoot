package com.example.demo.Service;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.LoginResponse;
import com.example.demo.DTO.SignInRequest;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.Jwt.JwtService;
@Service
public class UserServiceImplement implements UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private PasswordEncoder encodePassword;
	@Autowired
	private AuthenticationManager authenManager;
	@Autowired
	private JwtService jwtService;
	
	@Override
	public User saveUser(UserDTO userDTO) {
		User user = new User();
		user.setemail(userDTO.getEmail());
		user.setpassword(encodePassword.encode(userDTO.getPassword()));
		userRepo.save(user);
		return user;
	}

	@Override
	public void addRoletoUser(String email, String roleName) {
		User user = userRepo.getuserByEmail(email);
		Role role = roleRepo.findByroleName(roleName);
		user.getRole().add(role);
		role.getUsers().add(user);
		userRepo.save(user);
		roleRepo.save(role);
	}

 

 
	@Override
	public User findUserByEmail(String email) {
		User user = userRepo.getuserByEmail(email);
		return user;
	}

	@Override
	public LoginResponse handleLogin(LoginRequest req) {
		Authentication authentication = authenManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailService userDetails = (UserDetailService) authentication.getPrincipal();    
		List<String> roles = userDetails.getAuthorities().stream()
		        .map(item -> item.getAuthority())
		        .collect(Collectors.toList());
 		String accessToken = jwtService.generateJwtToken(authentication);
		return new LoginResponse(accessToken,"refresh","Login SucessFully");
	}
	@Override
	public LoginResponse handleSignup(SignInRequest req) {
		if (userRepo.existsByEmail(req.getEmail())) {
			return new LoginResponse("","","Email has existed");
		}
		if (userRepo.existsByUsername(req.getUserName())) {
			return new LoginResponse("","","UserName has used");
		}
		User user = new User();
 		user.setemail(req.getEmail());
		user.setusername(req.getUserName());
		user.setpassword(encodePassword.encode(req.getPassword()));
 		Set<Role>roles = new HashSet<>();
		Role role = roleRepo.findByroleName(req.getRole());
		roles.add(role);
		role.getUsers().add(user);
 		user.setRole(roles);
		userRepo.save(user);
 		Authentication authentication = authenManager.authenticate(new UsernamePasswordAuthenticationToken(user.getusername(),user.getpassword()));
 		System.out.println(user.getusername());
 		String accessToken = jwtService.generateTokenSignup(user);
		return new LoginResponse(accessToken,"refresh",req.getPassword());
	}

}


