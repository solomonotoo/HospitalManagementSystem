package com.hms.security.request;

import java.util.Set;

import com.hms.modules.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//A DTO class for signup request

public class SignupRequest {

	@NotBlank
	@Size(min = 4, max = 20)
	private String username;
	
	@NotBlank
	@Size(min = 4, max = 40)
	private String email;
	
	@NotBlank
	@Size(min = 5, max = 128)
	private String password;
	
	private Set<String> roles;
	
	

	public SignupRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SignupRequest(String username,String email, String password,
			Set<String> roles) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	
}
