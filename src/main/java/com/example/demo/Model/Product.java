package com.example.demo.Model;

import lombok.Data;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; 
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table; 

@Data
@Entity
@Table(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="productid")
	private Long productID;
	
	@Column(name="productname",length=100,nullable=false)
	private String productName;
	
	@Column(name="productcost",nullable=false)
	private Double productCost;
	
	@Column(name="productquantity",nullable=false)
	private Integer productQuantity;
	
	@ManyToMany(mappedBy = "productList")
	@JsonIgnore
	private Set<Order> orderList;
    public Set<Order> getOrderList() {
		return this.orderList;  
    }
    public void setOrderList(Set<Order> orderList) {
		this.orderList = orderList;  
    }
	public Long getProductID() {
        return productID;
    }
	  public int getproductQuantity() {
	        return productQuantity;
	    }

	    public void setproductQuantity(int productQuantity) {
	        this.productQuantity = productQuantity;
	    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductCost() {
        return productCost;
    }

    public void setProductCost(Double productCost) {
        this.productCost = productCost;
    }

}
