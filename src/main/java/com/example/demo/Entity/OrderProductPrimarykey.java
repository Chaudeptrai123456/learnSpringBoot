package com.example.demo.Entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Embeddable
public class OrderProductPrimarykey  implements Serializable{

	@JsonBackReference
	@ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "order_id")
	private Order order;
	@JsonBackReference
	@ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "product_id")
	private Product product;
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
}
