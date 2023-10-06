package com.example.Aiking.Service.Payment;

import com.example.Aiking.DTO.PaymentPOJO;
import com.example.Aiking.Entity.Payment;

public interface PaymentServiceimplement {

    public Payment addNewPayment(PaymentPOJO pojo,String userName) throws Exception;
    public Payment updatePayment(PaymentPOJO pojo) throws Exception;

}
