package com.example.demo.DTO;

import java.util.Set;

import com.example.demo.Model.Product;

public class OrderDTO {
	
	private String userName;
	private Double totalCost;
	private Set<Product> productList;
	
	public OrderDTO(String userName,Double totalCost,Set<Product> productList) {
		this.userName = userName;
		this.totalCost = totalCost;
		this.productList = productList;
	}
	

	public String getUserName() {
		return this.userName;
	}

	public Double gettotalCost() {
		return this.totalCost;
	}
 
	public Set<Product> getproductList() {
		return this.productList;
	}

}
