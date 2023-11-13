package com.example.Aiking.Service.User;

import com.example.Aiking.DTO.Auth.AuthRequest;
import com.example.Aiking.DTO.Auth.AuthResponse;
import com.example.Aiking.DTO.UserDTO;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.UserRepository;
import com.example.Aiking.Service.Gmail.GmailService;
import com.example.Aiking.Service.User.Implements.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@EnableCaching
public class UserService implements UserServiceImplement {
    @Autowired()
    private UserRepository userRepository;
    @Autowired
    private GmailService gmailService;
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository , GmailService gmailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.gmailService = gmailService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User handleUpdateUserInfo(UserDTO dto) throws Exception {
        Optional<User> user = userRepository.findUserByUserName(dto.getUserName());
        if (user.isPresent()) {
            user.get().setUserName(dto.getUserName());
            user.get().setPoint(dto.getPoint());
            user.get().setEmail(dto.getEmail());
            user.get().setFullName(dto.getFullName());
            user.get().setUpdateDate(new Date());
            user.get().setFullName(dto.getFullName());
            return userRepository.save(user.get());
        } else {
            throw new Exception("user has not existed");
        }
    }

    @Override
    public String sendOpt(String email, String userName) {
        String uuid = UUID.randomUUID().toString();
        try {
            gmailService.sendMessage("Opt for changing password","Opt " + uuid,"phamchaugiatu123@gmail.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        String optToken = redisService.saveOpt(uuid,email+"_"+userName);
        return uuid;
    }

    @Override
    public String handleChangPasswordUsingOpt(String userName, String opt,String newPassword) {
        User user = userRepository.findUserByUserName(userName).get();
//        String optRedis = redisService.findItemById(user.getEmail()+"_"+userName);
//        if (optRedis != null) {
            //change password
            user.setPassword(passwordEncoder.encode(newPassword.toString()));
            user.setUpdateDate(new Date());
            userRepository.save(user);
//        }
        return newPassword;
    }
}

