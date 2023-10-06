package com.example.Aiking.Controller;

import com.example.Aiking.DTO.PaymentPOJO;
import com.example.Aiking.Entity.Payment;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Service.Payment.PaymentSerivce;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    private PaymentSerivce paymentSerivce;
    @PostMapping("/add")
    public ResponseEntity<Payment> handleAddPayment(@RequestBody PaymentPOJO pay, HttpServletRequest request) throws Exception {
        String username = (String) request.getAttribute("username");
        Payment result = paymentSerivce.addNewPayment(pay,username);
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
    }
    @PostMapping("/update")
    public ResponseEntity<Payment> handleUpdatePayment(@RequestBody PaymentPOJO paymentPOJO) throws Exception {
        Payment result = paymentSerivce.updatePayment(paymentPOJO);
        return new ResponseEntity<>(result,HttpStatusCode.valueOf(200));
    }

}
