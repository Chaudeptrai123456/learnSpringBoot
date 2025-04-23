package se.chau.microservices.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage(){
        return  "custom_login";
    }
    @GetMapping("/user/register")
    public String registerPage(){
        return  "custom_regis";
    }
}