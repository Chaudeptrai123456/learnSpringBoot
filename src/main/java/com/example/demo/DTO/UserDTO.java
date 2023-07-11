package com.example.demo.DTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.demo.Entity.Role;

public class UserDTO {
	private String email;
	private String password;
	private List<String> roles ;

	public UserDTO(String email, String password, List<String> roles) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

 
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
