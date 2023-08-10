package com.example.demo.Service;

import com.example.demo.DTO.OrderDTO;
import com.example.demo.Entity.Order;

public interface OrderService {
	public Iterable<Order> getAllOrder();
	public Order createOrder(OrderDTO order);
	public Order updateOrder(Order order);
	
}
