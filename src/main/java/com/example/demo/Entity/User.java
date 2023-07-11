package com.example.demo.Entity;

 import java.util.HashSet;
import java.util.Set;
import com.example.demo.Entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.*;
@Entity
@Table(name="user")
public class User   {
	/**
	 * 
	 */
 	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private long id;
	@Column(name="email",nullable=false,length=100)
	private String email;
	@Column(name="username",nullable=false,length= 100)
	private String username;
	@Column(name="password",nullable=false)
	private String password;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", 
	             joinColumns = @JoinColumn(name = "user_id"),
	             inverseJoinColumns = @JoinColumn(name = "role_id"))
	 private Set<Role> roles = new HashSet<>();
 

	public void addRoleToList(Role role) {
		roles.add(role);
	}
	
	
	  public Long getuserid() {
		  return this.id;
	  }
	  
	  public void setusername(String username) {
		  this.username = username;
	  }
	  
	  public String getusername() {
		  return this.username;
	  }
	  
	  public void setemail(String email) {
		  this.email = email;
	  }
	  
	  public String getemail() {
		  return this.email;
	  }
	  
	  public void setpassword(String password) {
		  this.password = password;
	  }
	  
	  public String getpassword() {
		  return this.password;
	  }
	  public Set<Role> getRole() {
  		return this.roles;  
	  }

	  public void setRole(Set<Role> role) {
		  this.roles = role;
  	 
	  }
	  public void addRole(Role role) {
		  this.roles.add(role);
	  }


	
}
