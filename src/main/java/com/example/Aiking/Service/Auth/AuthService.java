package com.example.Aiking.Service.Auth;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;

public interface AuthService {
    AuthResponse login(AuthRequest loginDto);

    AuthResponse register(UserDTO user);

}
