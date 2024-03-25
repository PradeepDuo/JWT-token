package com.ff.secujwttoken.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

@RestController
public class DemoController {
	
	@GetMapping("/demo")
	public ResponseEntity<String> demo(){
		return ResponseEntity.ok("hello from secured URL");
	}
	
	@GetMapping("/admin_only")
//	@PreAuthorize("hasRole{'ADMIN'}")
	public ResponseEntity<String> adminOnly(){
		return ResponseEntity.ok("hello from admin only url");	
	}
	@GetMapping("/user_only")
	public ResponseEntity<String> userOnly(){
		return ResponseEntity.ok("hello from user only url");	}

}
