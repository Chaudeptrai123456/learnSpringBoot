package com.example.Aiking.Service.User;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.UserRepository;
import com.example.Aiking.Service.User.Implements.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserServiceImplement {
    @Autowired()
    private UserRepository userRepository;

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
        return null;
    }

    @Override
    public User handleUpdateUserInfo(UserDTO dto) throws Exception {
        Optional<User> user = userRepository.findUserByUserName(dto.getUserName());
        if (user.isPresent()) {
            user.get().setUserName(dto.getUserName());
            user.get().setPoint(dto.getPoint());
            user.get().setEmail(dto.getEmail());
            user.get().setFullName(dto.getFullName());
        } else {
            throw new Exception("user has not existed");
        }
        return userRepository.save(user.get());
    }
}

