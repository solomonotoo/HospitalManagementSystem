package com.hms.modules;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;


@Audited
@Entity
@Table(name = "users")
@JsonPropertyOrder({ "id", "username", "email", "account_non_locked", "acount_non_expired", "credentials_non_expired",
		"credentials_expiry_date", "account_expiry_date", "two_factor_secret", "is_two_factor_enabled",
		"sign_up_method" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty("username")
	@Column(name = "username", length = 45)
	@Length(min = 2, max = 45)
	private String userName;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	@Column(nullable = false, length = 128)
	private String password;

	@JsonProperty("account_non_locked")
	@Column(name = "account_non_locked")
	private boolean accountNonLocked = true;

	@JsonProperty("account_non_expired")
	@Column(name = "account_non_expired")
	private boolean accountNonExpired = true;

	@JsonProperty("credentials_non_expired")
	@Column(name = "credentials_non_expired")
	private boolean credentialsNonExpired = true;

	private boolean enabled = true;

	@JsonProperty("credentials_expiry_date")
	@Column(name = "credentials_expiry_date")
	private LocalDate credentialsExpiryDate;

	@JsonProperty("account_expiry_date")
	@Column(name = "account_expiry_date")
	private LocalDate accountExpiryDate;

	@JsonProperty("two_factor_secret")
	@Column(name = "two_factor_secret")
	private String twoFactorSecret;

	@JsonProperty("is_two_factor_enabled")
	@Column(name = "is_two_factor_enabled")
	private boolean isTwoFactorEnabled = false;

	@JsonProperty("sign_up_method")
	@Column(name = "sign_up_method")
	private String signUpMethod;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	Set<Role> roles = new HashSet();

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	public User() {
		super();
	}

	public User(String userName, String email, String password) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getTwoFactorSecret() {
		return twoFactorSecret;
	}

	public void setTwoFactorSecret(String twoFactorSecret) {
		this.twoFactorSecret = twoFactorSecret;
	}

	public boolean isTwoFactorEnabled() {
		return isTwoFactorEnabled;
	}

	public void setTwoFactorEnabled(boolean isTwoFactorEnabled) {
		this.isTwoFactorEnabled = isTwoFactorEnabled;
	}

	public String getSignUpMethod() {
		return signUpMethod;
	}

	public void setSignUpMethod(String signUpMethod) {
		this.signUpMethod = signUpMethod;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	// Custom setter for roles to handle JSON deserialization (both single and
	// array)
	@JsonProperty("roles")
	public void setRolesFromJson(Object roleNames) {
		if (roleNames instanceof String) {
			// Handle single role string
			addRole((String) roleNames);
		} else if (roleNames instanceof List) {
			// Handle list of roles
			for (Object roleName : (List<?>) roleNames) {
				addRole((String) roleName);
			}
		}
	}

	// Method to add a role from role name
	private void addRole(String roleName) {
		if (roleName != null) {
			Role role = new Role();
			role.setRoleName(UserRoles.valueOf(roleName)); // Use UserRoles enum to set the Role's name
			this.roles.add(role); // Add the Role to the set
		}
	}

	// method to add role to user
	public void addRole(Role role) {
		this.roles.add(role);
	}

	// builder methods
	public User id(Long id) {
		setId(id);
		return this;
	}

	public User userName(String userName) {
		setUserName(userName);
		return this;
	}

	public User password(String password) {
		setPassword(password);
		return this;
	}

	public User email(String email) {
		setEmail(email);
		return this;
	}

	public User accountEnabled(boolean enabled) {
		setEnabled(enabled);
		return this;
	}

	public User accountNonLocked(boolean accountNonLocked) {
		setAccountNonLocked(accountNonLocked);
		return this;
	}

	public User accountNonExpired(boolean accountNonExpired) {
		setAccountNonExpired(accountNonExpired);
		return this;

	}

	public User credentialsNonExpired(boolean credentialsNonExpired) {
		setCredentialsNonExpired(credentialsNonExpired);
		return this;
	}

	public User credentialsExpiryDate(LocalDate credentialsExpiryDate) {
		setCredentialsExpiryDate(credentialsExpiryDate);
		return this;
	}

	public User accountExpiryDate(LocalDate accountExpiryDate) {
		setAccountExpiryDate(accountExpiryDate);
		return this;
	}

	public User isTwoFactorEnabled(boolean isTwoFactorEnabled) {
		setTwoFactorEnabled(isTwoFactorEnabled);
		return this;
	}

	public User signUpMethod(String signUpMethod) {
		setSignUpMethod(signUpMethod);
		return this;
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
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

}
