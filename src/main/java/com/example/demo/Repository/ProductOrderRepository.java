package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Model.ProductOrder;

 

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
 
}
