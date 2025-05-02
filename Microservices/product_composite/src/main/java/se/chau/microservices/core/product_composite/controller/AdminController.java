package se.chau.microservices.core.product_composite.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/product-composite/page")
public class AdminController {
    @GetMapping
    public String index() {
        try {
            // Giả sử bạn kiểm tra token bằng cách decode nó (có thể thay bằng logic xác thực thực tế)

            // Nếu token hợp lệ, trả về trang index
            return "index";
        } catch (Exception e) {
            // Nếu token không hợp lệ, trả về lỗi 401 Unauthorized
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid access token.", e);
        }
    }

}
