package com.ff.secujwttoken.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ff.secujwttoken.entity.AuthenticationResponse;
import com.ff.secujwttoken.entity.Users;
import com.ff.secujwttoken.service.AutheticationService;

@RestController
public class AuthenticationController {
	
	private final AutheticationService authService;

	public AuthenticationController(AutheticationService authService) {
		super();
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody Users request){
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody Users request){
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
	
	
	
	
	
	
}
