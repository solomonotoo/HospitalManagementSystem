package com.hms.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.modules.Role;
import com.hms.modules.User;
import com.hms.modules.UserRoles;
import com.hms.repository.RoleRepository;
import com.hms.services.impl.UserServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestApiController {

	
	private UserServiceImpl userService;
	 private RoleRepository roleRepo;

	public UserRestApiController(UserServiceImpl userService) {
		super();
		this.userService = userService;
	}
	
	//handler method that list all users
//	public ResponseEntity<List<User>> listUsers(){
//		List<User> users = userService.listUsers();
//		return ResponseEntity.ok(users);
//	}
	
	@GetMapping
	public ResponseEntity<?> listUsers(){
		List<User> users = userService.listUsers();	
		return ResponseEntity.ok(users);
	}
	
	//handle method that create a new user
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody @Valid User user){
	
		User newUser = userService.createUser(user);
		
		return new ResponseEntity<User>(newUser,HttpStatus.CREATED);
	}
	
//	@PutMapping("/{id}")
//	public ResponseEntity<User> updateUser(@RequestBody @Valid User user, @PathVariable("id") Long id){
//		User updateUser = userService.updateUser(user, id);
//		return ResponseEntity.ok(updateUser);
//	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") Long id){
		 userService.deleteUser(id);
		 
		 return ResponseEntity.noContent().build();
	}
	
	//handler method that get user by email
	@PostMapping("/check_email")
	public String checkDuplicateEmail(Long id, String email) {
		return userService.isEmailUnique(id, email) ? "OK" : "Duplicate";
	}

}
