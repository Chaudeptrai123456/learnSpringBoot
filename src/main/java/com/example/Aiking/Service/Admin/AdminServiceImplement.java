package com.example.Aiking.Service.Admin;

import com.example.Aiking.DTO.RequestBlockUser;
import com.example.Aiking.Entity.User;

public interface AdminServiceImplement {

    String refreshPassword(String userName);
    User blockUser(RequestBlockUser req);

}
