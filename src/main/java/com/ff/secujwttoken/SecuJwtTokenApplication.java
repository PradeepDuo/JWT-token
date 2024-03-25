package com.ff.secujwttoken;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class SecuJwtTokenApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuJwtTokenApplication.class, args);
	}

}
