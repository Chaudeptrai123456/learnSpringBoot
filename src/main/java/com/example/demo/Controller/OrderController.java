package com.example.demo.Controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.AddProductoOrderReq;
import com.example.demo.Model.Order;
import com.example.demo.Model.Product;
import com.example.demo.Service.OrderServiceImp;
import com.example.demo.Service.ProductService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private OrderServiceImp service;
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> handleGetAllProductofOrder(@PathVariable(required = true) Long id ){
		Set<Product> order = service.getAllProductofOrder(id);		
		return new ResponseEntity<>(order,HttpStatus.ACCEPTED);		
	}
	@GetMapping("/all")
	public ResponseEntity<?> handleGetAllOrder(){
		Iterable<Order>a = service.getAllOrder();
		return new ResponseEntity<>(a,HttpStatus.ACCEPTED);		
	}
	@GetMapping("/create/{username}")
	public ResponseEntity<?> createNewOrder(@PathVariable(required=true) String username){
		Order neworder = service.createNewOrder(username);
		return new ResponseEntity<>(neworder,HttpStatus.ACCEPTED);		
	}
	@GetMapping("/addProducttoOrder")
	public ResponseEntity<?> createNewOrder1(@RequestBody AddProductoOrderReq req){
		Order  res = service.addNewProducttoOrder(req.getProducName(),req.getorderid(),req.getamount());
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);		
	}
	
}
