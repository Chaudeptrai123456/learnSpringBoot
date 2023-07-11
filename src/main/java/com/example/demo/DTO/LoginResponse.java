package com.example.demo.DTO;

public class LoginResponse {
	private String tokenAccess;
	private String tokenRefresh;
	private String message;
	
	public LoginResponse(String tokenAccess, String tokenRefresh, String message) {
		super();
		this.tokenAccess = tokenAccess;
		this.tokenRefresh = tokenRefresh;
		this.message = message;
	}
 
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTokenAccess() {
		return tokenAccess;
	}
	public void setTokenAccess(String tokenAccess) {
		this.tokenAccess = tokenAccess;
	}
	public String getTokenRefresh() {
		return tokenRefresh;
	}
	public void setTokenRefresh(String tokenRefresh) {
		this.tokenRefresh = tokenRefresh;
	}
	
}
