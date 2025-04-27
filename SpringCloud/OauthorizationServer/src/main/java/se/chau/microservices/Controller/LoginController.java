package se.chau.microservices.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Xóa thông tin xác thực
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // Invalidate session (optional, tùy thuộc vào cách quản lý session)
        request.getSession().invalidate();

        return "redirect:/login?logout"; // Hoặc trang nào đó em muốn redirect sau logout
    }
}