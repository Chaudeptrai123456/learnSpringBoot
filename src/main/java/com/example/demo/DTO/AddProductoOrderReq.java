package com.example.demo.DTO;

import lombok.*;

@Getter
@Setter
public class AddProductoOrderReq {
	private String productName;
	private Long orderid;
	private int amount;
	public void setProductName(String name) {
		this.productName = name;
	}
	public String getProducName() {
		return this.productName;
	}
	public void setorderid(Long orderid) {
		this.orderid = orderid;
	}
	public Long getorderid() {
		return this.orderid;
	}
	public void setamount(int amount) {
		this.amount = amount;
	}
	public int getamount() {
		return this.amount;
	}
}
