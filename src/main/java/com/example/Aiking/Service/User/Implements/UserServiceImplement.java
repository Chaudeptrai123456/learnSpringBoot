package com.example.Aiking.Service.User.Implements;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.User;

public interface UserServiceImplement {
    public User handleUpdateUserInfo(UserDTO user) throws Exception;
    String sendOpt(String email, String userName);
    String handleChangPasswordUsingOpt(String userName,String opt,String newPassword);
}
