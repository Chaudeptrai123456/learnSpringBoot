package com.example.Aiking.Service.Admin;

import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.UserRepository;
import com.example.Aiking.Service.Gmail.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.UUID;

@Service
public class AdminService implements AdminServiceImplement{
    @Autowired
    private GmailService gmailService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    public AdminService(GmailService gmailService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.gmailService = gmailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public String refreshPassword(String userName) {
        User user = userRepository.findUserByUserName("test1").get();
        UUID newPassword = UUID.fromString(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(newPassword.toString()));
        userRepository.save(user);
//        try {
//            gmailService.sendMail("Refresh Password", "New Password : "+newPassword,"phamchaugiatu123@gmail.com");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        try {
            gmailService.sendMessage("refreshPassword","New Password " + newPassword.toString(),"phamchaugiatu123@gmail.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newPassword.toString();
    }
}
