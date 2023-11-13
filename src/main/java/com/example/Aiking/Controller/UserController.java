package com.example.Aiking.Controller;

import com.example.Aiking.DTO.RequestChangePassword;
import com.example.Aiking.DTO.RequestSendOpt;
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
    @PostMapping("/sendOpt")
    public ResponseEntity<?> handSendOpt(@RequestBody RequestSendOpt requestSendOpt) {
        String result = userService.sendOpt(requestSendOpt.email(), requestSendOpt.userName());
        return new ResponseEntity<>(result+" "+"check your email ",HttpStatusCode.valueOf(200));
    }
    @PostMapping("/handleChangePasswordByOpt")
    public ResponseEntity<?> handleChangePasswordByOpt(@RequestBody RequestChangePassword req) {
        String result = userService.handleChangPasswordUsingOpt(req.userName(), req.opt(), req.newPassword());
        return new ResponseEntity<>(result,HttpStatusCode.valueOf(200));
    }
}
