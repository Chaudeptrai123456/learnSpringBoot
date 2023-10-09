package com.example.Aiking.Service.Payment;

import com.example.Aiking.DTO.PaymentPOJO;
import com.example.Aiking.Entity.Payment;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.PaymentRepository;
import com.example.Aiking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PaymentSerivce implements PaymentServiceimplement{
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Payment addNewPayment(PaymentPOJO pojo,String userName) throws Exception {
        try {
            User user = userRepository.findUserByUserName(userName).get();
            Payment payment = new Payment();
            payment.setCardId(pojo.getCardId());
            payment.setFullName(pojo.getFullName());
            payment.setNameCard(pojo.getCardId());
            payment.setCreateDate(new Date());
            payment.setUpdateDate(new Date());
            payment.setUser(user);
            user.addPaymentToList(payment);
            userRepository.save(user);
            return paymentRepository.save(payment);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Payment updatePayment(PaymentPOJO pojo) throws Exception{
        try {
            Boolean isPaymentExisted = paymentRepository.existsByCardId(pojo.getCardId());
            if (!isPaymentExisted) {
                return null;
            }
            Payment updatePayment = paymentRepository.findByCardId(pojo.getCardId()).get();
            updatePayment.setCardId(pojo.getCardId());
            updatePayment.setFullName(pojo.getFullName());
            updatePayment.setNameCard(pojo.getNameCard());
            updatePayment.setUpdateDate(new Date());
            return paymentRepository.save(updatePayment);
        } catch(Exception e) {
            System.out.println("test update payment "+ e.getMessage());
            throw  new Exception(e.getMessage());
        }
    }
}
