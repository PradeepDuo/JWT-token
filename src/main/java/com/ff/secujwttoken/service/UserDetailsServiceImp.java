package com.ff.secujwttoken.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ff.secujwttoken.repository.UsersRepository;
@Service
public class UserDetailsServiceImp implements UserDetailsService {
	
	private final UsersRepository repository;
	
	public UserDetailsServiceImp(UsersRepository repository) {
		this.repository = repository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUserName(username)
				.orElseThrow(()->new UsernameNotFoundException("user not found"));
	}

}
