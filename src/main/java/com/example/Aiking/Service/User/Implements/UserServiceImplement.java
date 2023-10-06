package com.example.Aiking.Service.User.Implements;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.User;

public interface UserServiceImplement {
    public AuthResponse handleLogin(AuthRequest request);
    public AuthResponse handleResgiter(UserDTO user);
}
