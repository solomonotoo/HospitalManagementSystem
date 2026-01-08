package com.hms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hms.modules.Role;
import com.hms.modules.User;
import com.hms.modules.UserDTO;
import com.hms.services.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminRestApiController {
	
	private UserService userService;
	
	public AdminRestApiController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getUserId(@PathVariable Long id){
		UserDTO user = userService.getUserId(id);
		return ResponseEntity.ok(user);
	}
	
	//handler method that get all user roles in the system
	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getRoles(){
		List<Role> roles = userService.getAllRoles();
		return ResponseEntity.ok(roles);
	}
	
//	//for a single role
//	//handler method to update user role
//	@PutMapping("/update-role")
//	public ResponseEntity<User> updateUserRole(@RequestParam Long userId, @RequestParam String roleName){
//		User user = userService.updateUserRole(userId, roleName);
//		return ResponseEntity.ok(user);
//	}
	
	//for a multiple roles
	//handler method to update user role
	@PutMapping("/update-role")
	public ResponseEntity<User> updateUserRole(@RequestParam Long userId, @RequestParam List<String> roleName){
		User user = userService.updateUserRole(userId, roleName);
		return ResponseEntity.ok(user);
	}
	
	//handler method to update user password
	@PutMapping("/update-password")
	public ResponseEntity<User> updateUserPassword(@RequestParam Long userId, @RequestParam String password){
		User user = userService.updateUserPassword(userId, password);
		return ResponseEntity.ok(user);
	}
	
	@PutMapping("/update-account-lock-status")
	public ResponseEntity<User> updateAccountLockStatus(@RequestParam Long userId, @RequestParam boolean lock){
		User user = userService.updateAccountLockStatus(userId, lock);
		return ResponseEntity.ok(user);
	}
	
	@PutMapping("/update-account-expiry-status")
	public ResponseEntity<User> updateAccountExpiryStaus(@RequestParam Long userId, @RequestParam boolean expire){
		User user = userService.updateAccountNonExpired(userId, expire);
		return ResponseEntity.ok(user);
	}
	
	@PutMapping("/update-credentials-expiry-status")
	public ResponseEntity<User> updateCredentialsExpiryStatus(@RequestParam Long userId, @RequestParam boolean expire){
		User user = userService.updateCrendentialsExpiryStatus(userId, expire);
		return ResponseEntity.ok(user);
	}

	
	@PutMapping("/update-account-enabled-status")
	public ResponseEntity<User> updateAccountEnabledStatus(@RequestParam Long userId, @RequestParam boolean enabled) {
		User user = userService.updateAccountEnabledStatus(userId, enabled);
		
		return ResponseEntity.ok(user);
	}
}
