package com.ff.secujwttoken.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ff.secujwttoken.entity.AuthenticationResponse;
import com.ff.secujwttoken.entity.Token;
import com.ff.secujwttoken.entity.Users;
import com.ff.secujwttoken.repository.TokenRepository;
import com.ff.secujwttoken.repository.UsersRepository;

@Service
public class AutheticationService {
	
	private final UsersRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AutheticationService(UsersRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.tokenRepository=tokenRepository;
		this.authenticationManager = authenticationManager;
	}

	public AuthenticationResponse register(Users request) {
		Users user = new Users();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setUserName(request.getUserName());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());
		
		user=repository.save(user);
		//this method will generate token 
		String jwt=jwtService.generateToken(user);
		//this will get all token a user have and remove valididty by setting them
//		as loggedout as true,
		revokeAllTokenByUser(user);
		//now save the generated token in the token table
		saveUserToken(user, jwt);
		
		
		return new AuthenticationResponse(jwt);
	}
	
	public AuthenticationResponse authenticate(Users request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		Users user = repository.findByUserName(request.getUsername()).orElseThrow();
		String token=jwtService.generateToken(user);
		//this method will save the token in table helpful in logout
		revokeAllTokenByUser(user);
		saveUserToken(user, token);
		return new AuthenticationResponse(token);
			
	}
	//this method will save token when it is called
	private void saveUserToken(Users user, String jwt) {
			Token token= new Token();
			token.setToken(jwt);
			token.setUsers(user);
			token.setLoggedOut(false);
			tokenRepository.save(token);
	}
	
//		this will get all token a user have and remove validity by setting them
//		as loggedOut as true,
	private void revokeAllTokenByUser(Users user) {
			List<Token> validTokenListByUsers = tokenRepository.findAllTokenByUsers(user.getId());
			if(!validTokenListByUsers.isEmpty()) {
//				this will set every available token as invalid by setting loggedOut as true 
				validTokenListByUsers.forEach(t->t.setLoggedOut(true));
			}
			//update all token to database
			tokenRepository.saveAll(validTokenListByUsers);
		}

	

}
