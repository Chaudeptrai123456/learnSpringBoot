package com.example.Aiking.Controller;

import com.example.Aiking.DTO.PaymentPOJO;
import com.example.Aiking.Entity.Payment;
import com.example.Aiking.Service.Payment.PaymentSerivce;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/payment")
public class PaymentController {
    @Autowired
    private PaymentSerivce paymentSerivce;
    @PostMapping("/add")
    public ResponseEntity<?> handleAddPayment(@RequestBody PaymentPOJO pay, HttpServletRequest request) throws Exception {
        try {
            String username = (String) request.getAttribute("username");
            Payment result = paymentSerivce.addNewPayment(pay,username);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    @PostMapping("/update")
    public ResponseEntity<Payment> handleUpdatePayment(@RequestBody PaymentPOJO paymentPOJO) throws Exception {
        Payment result = paymentSerivce.updatePayment(paymentPOJO);
        return new ResponseEntity<>(result,HttpStatusCode.valueOf(200));
    }
}
