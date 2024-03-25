package com.ff.secujwttoken.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ff.secujwttoken.service.JwtService;
import com.ff.secujwttoken.service.UserDetailsServiceImp;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter  {
	
	private final JwtService jwtService;
	private final UserDetailsServiceImp userDetailsService;
	
	

	public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImp userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain)throws ServletException, IOException {
		String authHeader= request.getHeader("Authorization");
		//Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJwcmFkZWVwIiwiaWF0IjoxNzEwMzE0NDE2LCJleHAiOjE3MTA0MDA4MTZ9.L7S6b-Ux03dYBerVyPIXO8VwAL7SjODJn_XNsZWV1_XBDp7MCGj7JBManeQApfZU
//		this is string stored in authheader variable
		
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		String token= authHeader.substring(7);
		String userName = jwtService.extractUserName(token);
		
		if (userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
			
			if(jwtService.isValid(token,userDetails )) {
				UsernamePasswordAuthenticationToken authToken= new 
						UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				System.out.println("authToken ::"+authToken);
//				this is the authtoken : which is extracted from incoming token
				//authToken ::UsernamePasswordAuthenticationToken [Principal=com.ff.secujwttoken.entity.Users@60f6cc4f, 
//				Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ADMIN]]
			}
		}
//		this method is called at the end of each filter which aids in passing the request and response to next filter
		filterChain.doFilter(request, response);
	}

}
