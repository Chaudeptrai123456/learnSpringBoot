package com.example.demo.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.lang.Long;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.stream.Collectors;

import java.util.*;
public class UserDetailService  implements UserDetails {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	  private String username;

	  private String email;

	  @JsonIgnore
	  private String password;

	  private Collection<? extends GrantedAuthority> authorities;

	  public UserDetailService (Long id, String username, String email, String password,
	      Collection<? extends GrantedAuthority> authorities) {
	    this.id = id;
	    this.username = username;
	    this.email = email;
	    this.password = password;
	    this.authorities = authorities;
	  }

	  public static UserDetailService build(User user) {
		  List<GrantedAuthority> authorities = user.getRole().stream()
			        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
			        .collect(Collectors.toList());
 
	    return new UserDetailService(
	        user.getuserid(), 
	        user.getusername(), 
	        user.getemail(),
	        user.getpassword(), 
	        authorities);
	  }

	  @Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
	    return authorities;
	  }

	  public Long getId() {
	    return id;
	  }

	  public String getEmail() {
	    return email;
	  }

	  @Override
	  public String getPassword() {
	    return password;
	  }

	  @Override
	  public String getUsername() {
	    return username;
	  }

	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }

	 

}
