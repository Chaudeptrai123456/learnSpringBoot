package com.example.demo.Entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.*;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@Table(name="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="orderid")
	private Long orderID;
	
	@Column(name="date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate date;
	@Column(name="username",length=100)
	private String userName;
	@Column(name="status",length=100)
	private String status;
	
//	@OneToMany(cascade = CascadeType.ALL,mappedBy="pk.order")
	@JsonManagedReference
    @OneToMany(mappedBy = "pk.order")
	private List<OrderProduct> listProduct;
	@Transient
	public Double getTotalOrder() {
		Double total = 0D;
		List<OrderProduct> products = getListProduct();
		for (OrderProduct index : products) {
			total += index.getTotalPrice();
		}
		return total;
	}
	@Transient
	public int getNumberOfProducts() {
	   return this.listProduct.size();
	}
	public Long getOrderID() {
		return orderID;
	}
	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<OrderProduct> getListProduct() {
		return listProduct;
	}
	public void setListProduct(List<OrderProduct> listProduct) {
		this.listProduct = listProduct;
	}
	
}
