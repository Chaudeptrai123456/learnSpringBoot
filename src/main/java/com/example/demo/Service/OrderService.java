package com.example.demo.Service;

import java.util.Set;

import com.example.demo.Model.Order;
import com.example.demo.Model.Product;

public interface OrderService {
	public Set<Product> getAllProductofOrder(Long orderID);
	public Order addNewProducttoOrder(String productName,Long orderID,int amount);
	public Order createNewOrder(String userName);
}
