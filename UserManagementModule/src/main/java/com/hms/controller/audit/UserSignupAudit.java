package com.hms.controller.audit;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Audited
@Table(name = "user_signup_audit")
public class UserSignupAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String email;
	
	@JsonProperty("signup_time")
	private LocalDateTime signupTime;
	
	@JsonProperty("sign_up_time")
	private String signUpMethod;
	
	public UserSignupAudit() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserSignupAudit(String username, String email, LocalDateTime signupTime, String signUpMethod) {
		super();
		this.username = username;
		this.email = email;
		this.signupTime = signupTime;
		this.signUpMethod = signUpMethod;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getSignupTime() {
		return signupTime;
	}

	public void setSignupTime(LocalDateTime signupTime) {
		this.signupTime = signupTime;
	}

	public String getSignUpMethod() {
		return signUpMethod;
	}

	public void setSignUpMethod(String signUpMethod) {
		this.signUpMethod = signUpMethod;
	}
	
	
}
