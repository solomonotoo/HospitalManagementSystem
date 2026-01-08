package com.hms.modules;

import java.time.Instant;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String token;
	
	@JsonProperty("is_token_used")
	@Column(name = "is_token_used")
	private boolean istokenUsed;
	
	@JsonProperty("token_expiry_date")
	@Column(nullable = false)
	private Instant tokenExpiryDate;
	
	//for the user the token belongs to
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public PasswordResetToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public PasswordResetToken(String token, Instant tokenExpiryDate, User user) {
		super();
		this.token = token;
		this.tokenExpiryDate = tokenExpiryDate;
		this.user = user;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean istokenUsed() {
		return istokenUsed;
	}

	public void setIstokenUsed(boolean istokenUsed) {
		this.istokenUsed = istokenUsed;
	}

	public Instant getTokenExpiryDate() {
		return tokenExpiryDate;
	}

	public void setTokenExpiryDate(Instant tokenExpiryDate) {
		this.tokenExpiryDate = tokenExpiryDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PasswordResetToken other = (PasswordResetToken) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}
