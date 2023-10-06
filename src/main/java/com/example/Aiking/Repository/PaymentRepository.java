package com.example.Aiking.Repository;

import com.example.Aiking.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Boolean  existsByCardId(String carid);
    Optional<Payment> findByCardId(String carid);
}
