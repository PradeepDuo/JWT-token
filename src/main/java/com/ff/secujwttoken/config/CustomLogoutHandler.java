package com.ff.secujwttoken.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.ff.secujwttoken.entity.Token;
import com.ff.secujwttoken.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class CustomLogoutHandler implements LogoutHandler {
	
	@Autowired
	private TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, 
						HttpServletResponse response, 
						Authentication authentication) {
		String authHeader= request.getHeader("Authorization");
		//Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJwcmFkZWVwIiwiaWF0IjoxNzEwMzE0NDE2LCJleHAiOjE3MTA0MDA4MTZ9.L7S6b-Ux03dYBerVyPIXO8VwAL7SjODJn_XNsZWV1_XBDp7MCGj7JBManeQApfZU
//		this is string stored in authheader variable
		
		if(authHeader == null || !authHeader.startsWith("Bearer")) {
			return;
		}
		String token= authHeader.substring(7);
		
		Token storedToken = tokenRepository.findByToken(token).orElse(null);
		if(token!=null) {
			storedToken.setLoggedOut(true);
			tokenRepository.save(storedToken);
		}
		
		
	}

}
