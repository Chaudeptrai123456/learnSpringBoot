package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


 
import com.example.demo.Model.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
	@Query(value="SELECT * FROM orders o WHERE o.orderid = :id",nativeQuery = true)
	public Order findByOrderID(Long id);
	
	@Query(value="SELECT * FROM orders o WHERE o.namecustomer like :username",nativeQuery = true)
	public Order findByUserName(String username);
 
}
