package com.ff.secujwttoken.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Token {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String token;
	private boolean isLoggedOut;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users users;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isLoggedOut() {
		return isLoggedOut;
	}
	public void setLoggedOut(boolean isLoggedOut) {
		this.isLoggedOut = isLoggedOut;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	

}
