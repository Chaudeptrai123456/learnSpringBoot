package com.example.Aiking.Service.User;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.Role;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.RoleRepository;
import com.example.Aiking.Repository.UserRepository;
import com.example.Aiking.Service.Jwt.JwtService;
import com.example.Aiking.Service.User.Implements.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Oauth2UserService implements UserServiceImplement {
    @Autowired()
    private UserRepository userRepository;
    @Autowired()
    private RoleRepository roleRepository;
    @Autowired()
    private JwtService jwtService;
    @Override
    public AuthResponse handleLogin(AuthRequest request) {
        Optional user = userRepository.findUserByUserName(request.getUserName());
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            User newUser = (User) user.get();
            userRepository.save(newUser);
            AuthResponse result = new AuthResponse("acctoken",token);
            return result;
        }
        return null;
    }

    @Override
    public AuthResponse handleResgiter(UserDTO user) {
        AuthResponse authResponse = new AuthResponse("","");
        try {
            User newUser = new User();
            System.out.println("test user service " + newUser.getEmail());
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            newUser.setPoint(newUser.getPoint());
            newUser.setUserName(user.getUserName());
            newUser.setCreateDate(new Date());
            newUser.setUpdateDate(new Date());
            Role role = roleRepository.findByRoleName("USER").get();
            Set<Role> roleList = new HashSet<>();
            roleList.add(role);
            newUser.setRoleList(roleList);
            userRepository.save(newUser);
        } catch (Exception err) {
            System.out.print(err.toString());
        }
        return authResponse;
    }
}
