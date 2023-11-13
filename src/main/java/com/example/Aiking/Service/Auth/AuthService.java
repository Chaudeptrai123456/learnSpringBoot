package com.example.Aiking.Service.Auth;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.User;

public interface AuthService {
    AuthResponse login(AuthRequest loginDto);

    User register(UserDTO user);

}
