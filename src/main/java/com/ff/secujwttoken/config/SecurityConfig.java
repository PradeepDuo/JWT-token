package com.ff.secujwttoken.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ff.secujwttoken.filter.JwtAuthenticationFilter;
import com.ff.secujwttoken.service.UserDetailsServiceImp;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final UserDetailsServiceImp userDetailsServiceImp;
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	private final CustomLogoutHandler logoutHandler;
	
	
	public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp, JwtAuthenticationFilter jwtAuthenticationFilter,
			CustomLogoutHandler logoutHandler) {
		this.userDetailsServiceImp = userDetailsServiceImp;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.logoutHandler=logoutHandler;
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req ->req.requestMatchers("/login/**","/register/**")
				.permitAll()
				.requestMatchers("/admin_only/**").hasAuthority("ADMIN")
				.requestMatchers("/user_only/**").hasAuthority("USER")			
				.anyRequest()
				.authenticated()
				).userDetailsService(userDetailsServiceImp)
				.sessionManagement(session->session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(l->l.logoutUrl("/logout")
						    .addLogoutHandler(logoutHandler)
						    .logoutSuccessHandler(
								(request,response,authentication)->SecurityContextHolder.clearContext()
						))
				.build();	
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
		
	}
	

}
