package com.example.Aiking.Controller;

import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Service.User.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/update")
    public ResponseEntity<User> handleUpdateUser(@RequestBody UserDTO req) throws Exception {
        User result = userService.handleUpdateUserInfo(req);
        return new ResponseEntity<User>(result,HttpStatusCode.valueOf(200));
    }




}
