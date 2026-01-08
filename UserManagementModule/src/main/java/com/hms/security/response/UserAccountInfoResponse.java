package com.hms.security.response;

import java.time.LocalDate;
import java.util.List;

//this class is dto class that represent response for user account information details. Any time we request for a user information
//or account details of authenticated user,this class will give that information
public class UserAccountInfoResponse {
	private Long id;
	private String username;
	private String email;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private LocalDate credentialsExpiryDate;
	private LocalDate accountExpiryDate;
	private boolean isTwoFactorEnabled;
	private List<String> roles;

	public UserAccountInfoResponse(Long id, String username, String email, boolean accountNonLocked,
			boolean accountNonExpired, boolean credentialsNonExpired, boolean enabled, LocalDate credentialsExpiryDate,
			LocalDate accountExpiryDate, boolean isTwoFactorEnabled, List<String> roles) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.accountNonLocked = accountNonLocked;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.credentialsExpiryDate = credentialsExpiryDate;
		this.accountExpiryDate = accountExpiryDate;
		this.isTwoFactorEnabled = isTwoFactorEnabled;
		this.roles = roles;
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

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LocalDate getCredentialsExpiryDate() {
		return credentialsExpiryDate;
	}

	public void setCredentialsExpiryDate(LocalDate credentialsExpiryDate) {
		this.credentialsExpiryDate = credentialsExpiryDate;
	}

	public LocalDate getAccountExpiryDate() {
		return accountExpiryDate;
	}

	public void setAccountExpiryDate(LocalDate accountExpiryDate) {
		this.accountExpiryDate = accountExpiryDate;
	}

	public boolean isTwoFactorEnabled() {
		return isTwoFactorEnabled;
	}

	public void setTwoFactorEnabled(boolean isTwoFactorEnabled) {
		this.isTwoFactorEnabled = isTwoFactorEnabled;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

}
