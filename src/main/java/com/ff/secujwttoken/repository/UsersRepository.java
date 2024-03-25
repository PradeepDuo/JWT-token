package com.ff.secujwttoken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ff.secujwttoken.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{
	
	public Optional<Users> findByUserName(String username);

}
