package com.example.Aiking.Service.Auth;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.Role;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.RoleRepository;
import com.example.Aiking.Repository.UserRepository;
import com.example.Aiking.Service.Jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImplement  implements AuthService {
    private AuthenticationManager authenticationManager;

    private RoleRepository roleRepository;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthServiceImplement(AuthenticationManager authenticationManager, RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(AuthRequest loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(),loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken(authentication);
        return new AuthResponse(token,token);
    }

    @Override
    public User register(UserDTO user){
        AuthResponse authResponse = new AuthResponse("","");
        User newUser = new User();
        try {
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setPoint(user.getPoint());
            newUser.setUserName(user.getUserName());
            newUser.setCreateDate(new Date());
            newUser.setUpdateDate(new Date());
            newUser.setBlock(Boolean.TRUE);
            newUser.setFullName(user.getFullName());
            Role role = roleRepository.findByRoleName("user").get();
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
        return newUser;
    }
}
