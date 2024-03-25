package com.ff.secujwttoken.service;

import java.util.Date;

import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ff.secujwttoken.entity.Users;
import com.ff.secujwttoken.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

//this class is used for generate secret key and token 
@Service
public class JwtService {
	@Autowired
	private TokenRepository tokenRepository;
	
	
	//this is a 256bit encryption key got from "https://asecuritysite.com/".
	private final String SECRET_KEY="7ba198212ca2d04e402d36add88d6d3ce861148b7a2aad0699a59807b49f9dec";
	
	//every token should have subject, issue date and expiration date and encryption key
	public String generateToken(Users user) {
		String token =Jwts.builder().subject(user.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date (System.currentTimeMillis()+ 24*60*60*1000))
				//this is used to encrypt the jwt token with cryptographic algorithm
//				we can also use other algorithms for this method as argument
				.signWith(getSigninKey())
				.compact();
		return token;
	}
	
	//this method will generate a secret key 
	private SecretKey getSigninKey() {
		//this is used to convert the secret key into byte[] which is an input for HMAC-SHA algorithm
		byte[] keyBytes= Decoders.BASE64URL.decode(SECRET_KEY);
//		here,secret key is generated using Keys a utility class with HMAC-SHA which is typically used 
//		in network protocols
		return Keys.hmacShaKeyFor(keyBytes);
	}
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSigninKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	public <T> T extractClaim(String token ,Function<Claims,T> resolver ) {
		Claims claims= extractAllClaims(token);
		return resolver.apply(claims);	
	}
	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	public boolean isValid(String token , UserDetails users) {
		String userName=extractUserName(token);
		//used to validate the token only valid if LoggedOut in token is false
		boolean isValidToken= tokenRepository.findByToken(token).map(t->!t.isLoggedOut()).orElse(false);
		
		return (userName.equals(users.getUsername())) && !isTokenExpired(token)&& isValidToken;
		
	}
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
}
