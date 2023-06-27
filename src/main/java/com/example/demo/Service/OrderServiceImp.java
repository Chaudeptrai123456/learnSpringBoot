package com.example.demo.Service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

 import com.example.demo.Model.Order;
import com.example.demo.Model.Product;
 import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductOrderRepository;
import com.example.demo.Repository.ProductRepository;
 
@Service
public class OrderServiceImp implements OrderService {

	@Autowired
	private OrderRepository repo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
 	private ProductOrderRepository productorderRepo;
	public Iterable<Order> getAllOrder() {
		return repo.findAll();
	}
	@Override
	public Set<Product> getAllProductofOrder(Long orderID) {
		Order order = repo.findByOrderID(orderID);
		return order.getProductList();
	}

	
	@Override
	public Order addNewProducttoOrder(String productName, Long orderID,int amount) {

		Order order = repo.findByOrderID(orderID);
		Product product = productRepo.findProductByName(productName);
		//productorderRepo.updateProductinOrder(product.getProductID().longValue(), orderID.longValue(), amount);
 		order.getProductList().add(product);
		//order.setProductList(product);
		product.getOrderList().add(order);
		repo.save(order);
		productRepo.save(product);
		return order;
	}

	@Override
	public Order createNewOrder(String userName) {
		Order newOrder = new Order();
		newOrder.setnameCustomert(userName);
		newOrder.setTotalCost(0.0D);
		repo.save(newOrder);
		return newOrder;
	}
	 

}
