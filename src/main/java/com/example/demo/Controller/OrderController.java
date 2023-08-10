package com.example.demo.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.OrderDTO;
import com.example.demo.Entity.Order;
import com.example.demo.Entity.OrderProduct;
import com.example.demo.Entity.Product;
import com.example.demo.Service.OrderSerivceImplement;
import com.example.demo.Service.ProductServiceImplement;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	@Autowired
	private ProductServiceImplement productService;
	@Autowired
	private OrderSerivceImplement orderService;
	@GetMapping("/allProdutct")
	public ResponseEntity<?> testAdmin(){
		List<Product> list = productService.getAllProduct();
		return new ResponseEntity<List<Product>>(list,HttpStatus.ACCEPTED);	
	}
	
//	@PostMapping("/create")
//	public ResponseEntity<?> createOrder(@RequestBody OrderDTO order){
//		orderService.createOrder(order);
//		return new ResponseEntity<>(order,HttpStatus.ACCEPTED);	
//	}
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(){
		OrderDTO order = new OrderDTO();
		Order order1 = orderService.createOrder(order);
		return new ResponseEntity<>(order1,HttpStatus.ACCEPTED);	
	}
}
