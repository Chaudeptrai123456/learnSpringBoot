package com.example.Aiking.Controller;

import com.example.Aiking.DTO.requestRefreshPassword;
import com.example.Aiking.Service.Admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @PostMapping("/refreshPassword")
    public ResponseEntity<?> handleRefreshPassword(@RequestBody requestRefreshPassword userName) {
        String result = adminService.refreshPassword(userName.getUserName());
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
    }


}
