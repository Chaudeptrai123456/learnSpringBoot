package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
public class OrderProduct {
	@EmbeddedId
    @JsonIgnore
	private OrderProductPrimarykey pk;
	private int quantity;
	
	public OrderProduct(Order order,Product product, int quantity) {
		super();
		pk = new OrderProductPrimarykey();
		pk.setOrder(order);
		pk.setProduct(product);
		this.quantity = quantity;
	}

	public OrderProductPrimarykey getPk() {
		return pk;
	}

	public void setPk(OrderProductPrimarykey pk) {
		this.pk = pk;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Transient
    public Product getProduct() {
        return this.pk.getProduct();
    }

    @Transient
    public Double getTotalPrice() {
        return getProduct().getCost() * this.getQuantity();
    }
}
