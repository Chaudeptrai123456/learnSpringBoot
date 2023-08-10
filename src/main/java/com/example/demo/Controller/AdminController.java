package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.Product;
import com.example.demo.Service.ProductServiceImplement;
import java.util.*;
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	private ProductServiceImplement service;
	
	@GetMapping("/test")
	public ResponseEntity<?> testAdmin(){
		return new ResponseEntity<String>("admin",HttpStatus.ACCEPTED);
	}
	@GetMapping("/product")
	public ResponseEntity<?> getAllProduct(){
		List<Product> list = service.getAllProduct();
		return new ResponseEntity<List<Product>>(list,HttpStatus.ACCEPTED);
	}
	@PostMapping("/create")
	public ResponseEntity<?> createNewProduct(@RequestBody ProductDTO newProduct){
		Product product = service.addNewProduct(newProduct);
		return new ResponseEntity<Product>(product,HttpStatus.ACCEPTED);
	}
	
}
