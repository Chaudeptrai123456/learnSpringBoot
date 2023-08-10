package com.example.demo.Service;
import java.util.ArrayList;

import com.example.demo.Entity.*;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.OrderDTO;
import com.example.demo.DTO.OrderProductDTO;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductRepository;
@Service
public class OrderSerivceImplement implements OrderService {
	@Autowired
	private OrderRepository repo;
	@Autowired
	private ProductRepository productRepo;
	
	@Override
	public Iterable<Order> getAllOrder() {
		return repo.findAll();
	}
	
	@Override
	public Order createOrder(OrderDTO order) {
		Order newOrder = new Order();
		newOrder.setDate(LocalDate.now());
		newOrder.setUserName(order.getUserName());
		List<OrderProduct> result = new ArrayList<>();
//		for (OrderProductDTO index : order.getListorder()) {
//			Product product  = productRepo.getProductByID(index.getProductID());
//			OrderProduct orderProduct = new OrderProduct(newOrder,product,index.getQuantity());
//			result.add(orderProduct);
//		}
		Product product  = productRepo.getProductByID((long) 1);
		Product product1  = productRepo.getProductByID((long) 2);

		result.add(new OrderProduct(newOrder, product, 1));
		result.add(new OrderProduct(newOrder, product1, 2));
		System.out.print("Order service" + result);
		newOrder.setListProduct(result);
		repo.save(newOrder);
		return newOrder;
	}

	@Override
	public Order updateOrder(Order order) {
		repo.save(order);
		return order;
	}

}
