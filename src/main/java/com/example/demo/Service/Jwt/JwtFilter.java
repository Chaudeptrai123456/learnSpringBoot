package com.example.demo.Service.Jwt;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.Service.UserDetailsServiceImple;
import com.example.demo.Service.UserServiceImplement;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;    
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
 public class JwtFilter extends OncePerRequestFilter{
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsServiceImple userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try { 
			 String jwt = parseJwt(request);
		      if (jwt != null && jwtService.validateJwtToken(jwt)) {
		        String username = jwtService.getUserNameFromJwtToken(jwt);
		        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		        UsernamePasswordAuthenticationToken authentication =
		            new UsernamePasswordAuthenticationToken(
		                userDetails,
		                null,
		                userDetails.getAuthorities());
		        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		        SecurityContextHolder.getContext().setAuthentication(authentication);			
		      }
		} catch(Exception e) {
			System.out.print(e.getMessage().toString());
		}
		filterChain.doFilter(request, response);
	}
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}
}
