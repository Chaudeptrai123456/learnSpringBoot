package com.example.Aiking.Service.User.Implements;

import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsServiceImplement implements UserDetails {

    private User user;
    @Autowired
    private UserRepository userRepository;


    public UserDetailsServiceImplement(User user) {
        this.user = user;
    }

    public UserDetails build() {
        Collection<? extends GrantedAuthority> authorities = this.getAuthorities();
        return new org.springframework.security.core.userdetails.User(
                this.user.getUsername(),
                this.user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        User user  = userRepository.findByUsernameOrEmail(this.user.getUsername(),this.user.getEmail()).orElseThrow(()->new UsernameNotFoundException("User has not existed"));
        Set<SimpleGrantedAuthority> authorities = user.getRoleList().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getBlock();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
