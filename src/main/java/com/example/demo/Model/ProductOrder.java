package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name="product_order")
@Entity
@Data
public class ProductOrder {
	@Id
	@Column(name="orderid")
	private Long orderID;
	@Column(name="productid")
	private Long productID;
	@Column(name="amount")
	private int amount;
	public void setorderID(Long orderID) {
        this.orderID = orderID;
    }

    public Long getProductCost() {
        return orderID;
    }
    public void setproductid(Long productid) {
        this.productID = productid;
    }

    public Long getproductidt() {
        return productID;
    }
    public void setamount(int amount) {
        this.amount = amount;
    }

    public int getamount() {
        return amount;
    }
}
