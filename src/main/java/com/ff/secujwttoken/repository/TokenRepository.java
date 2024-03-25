package com.ff.secujwttoken.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ff.secujwttoken.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Integer>{
	
	@Query("select t from Token t inner join Users u on t.users.id= u.id where t.users.id= :userId")
	public List<Token> findAllTokenByUsers(int userId);
	
	public Optional<Token> findByToken (String token);

}
