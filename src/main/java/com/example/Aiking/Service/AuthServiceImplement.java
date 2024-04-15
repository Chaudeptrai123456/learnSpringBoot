package com.example.Aiking.Service;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.Role;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.RoleRepository;
import com.example.Aiking.Repository.UserRepository;
import com.example.Aiking.Service.Auth.AuthService;
import com.example.Aiking.Service.Jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImplement  implements AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
//    public AuthServiceImplement(AuthenticationManager authenticationManager, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
//        this.authenticationManager = authenticationManager;
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtService = jwtService;
//    }

    @Override
    public AuthResponse login(AuthRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return new AuthResponse(token,token);
    }

    @Override
    public AuthResponse register(UserDTO user){
        AuthResponse authResponse = new AuthResponse("","");
        try {
            User newUser = new User();
            System.out.println("test user service " + newUser.getEmail());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setPoint(newUser.getPoint());
            newUser.setUserName(user.getUserName());
            newUser.setCreateDate(new Date());
            newUser.setUpdateDate(new Date());
            Role role = roleRepository.findByRoleName("USER").get();
            Set<Role> roleList = new HashSet<>();
            roleList.add(role);
            newUser.setRoleList(roleList);
            userRepository.save(newUser);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);
            authResponse.setAccessToken(token);
            authResponse.setRefreshToken(token);
        } catch (Exception err) {
            System.out.print(err.toString());
        }
        return authResponse;
    }
    public void genaterRole(){
        try {
            Role newrole = new Role();
            newrole.setRoleName("USER");
            this.roleRepository.save(newrole);
            Role newrole1 = new Role();
            newrole1.setRoleName("ADMIN");
            this.roleRepository.save(newrole1);
        } catch (Exception err) {
            System.out.println(err);
        }
    }
}
