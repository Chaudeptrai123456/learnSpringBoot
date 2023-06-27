package com.example.demo.Model;

import java.util.Set;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
 

@Entity
@Data
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="orderid")
	private Long orderID;
	
	@Column(name="namecustomer",length=100,nullable=false)
	private String nameCustomer;
	
	@Column(name="totalcost",nullable = false)
	private Double totalCost;
	
    @ManyToMany
    @JoinTable(name="user_role",
            joinColumns = @JoinColumn(name="orderid",referencedColumnName = "orderid"),
            inverseJoinColumns = @JoinColumn(name="productid",referencedColumnName = "productid")
    )
	private Set<Product> productList;
	   public Set<Product> getProductList() {
   		return this.productList;  
   }
 
   public void setProductList(Product product) {
   	this.productList.add(product);
   	product.getOrderList().add(this);
   }
   public void setTotalCost(Double total) {
	   this.totalCost = total;
   }
   public Double getTotalCost() {
	   return this.totalCost;
   }
   public void setnameCustomert(String userName) {
	   this.nameCustomer = userName;
   }
   public String getnameCustomer() {
	   return this.nameCustomer;
   }
}
