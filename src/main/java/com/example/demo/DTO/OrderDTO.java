package com.example.demo.DTO;

import java.util.List;

import com.example.demo.Entity.Order;

public class OrderDTO {

	private String userName;
	private List<OrderProductDTO> listorder;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<OrderProductDTO> getListorder() {
		return listorder;
	}
	public void setListorder(List<OrderProductDTO> listorder) {
		this.listorder = listorder;
	}
	

	
}
