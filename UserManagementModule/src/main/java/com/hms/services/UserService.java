package com.hms.services;

import java.util.List;

import com.hms.modules.PasswordResetToken;
import com.hms.modules.Role;
import com.hms.modules.User;
import com.hms.modules.UserDTO;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface UserService {

	public List<User> listUsers();
	public User createUser(User user);
	public User getUserById(Long userId);
	public UserDTO getUserId(Long userId);
	//public User updateUser(User user,Long userId);
	public void deleteUser(Long userId);
	public boolean isEmailUnique(Long id, String email);
	public User findByUsername(String username);
	//for a single role
	//public User updateUserRole(Long userId, String roleName);
	
	//for multiple roles
	public User updateUserRole(Long userId, List<String> roleName);
	public User updateUserPassword(Long userId, String password);
	public User updateAccountLockStatus(Long userId, boolean lock);
	public User updateAccountNonExpired(Long userId, boolean expire);
	public User updateCrendentialsExpiryStatus(Long userId, boolean expired);
	public User updateAccountEnabledStatus(Long userId, boolean enabled);
	public List<Role> getAllRoles();
	//public void generatePasswordResetToken(String email);
	public User generatePasswordResetToken(String email);
	public User resetPassword(String passwordResetToken, String newPassword);
	public User findByEmail(String email);
	public User registerUser(User newUser);
	boolean validate2FaCode(Long userId, int code);
	GoogleAuthenticatorKey generate2FASecret(Long userId);
	void enable2FA(Long userId, boolean enabled);
	
}
