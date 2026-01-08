package com.hms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hms.exceptions.UserNotFoundException;
import com.hms.modules.User;
import com.hms.repository.UserRepository;

//Nb  the main purpose of this class is to get the ID and username of logged in user or authenticated user

@Component
public class AuthUtil {

	@Autowired
	UserRepository userRepo;
	
	//method that get the ID of Authenticated user
	public Long getLoggedInUserID() {
		//get the security context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		//get ID of authenticated user
		User user = userRepo.findByUserName(authentication.getName());
		
		if(user == null) {
			throw new UserNotFoundException("Could not find user");
		}
		
		return user.getId();
	}
	
	//method that get username of authenticated user
	public User getloggedInUsername() {
		//get the security context holder
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		//get ID of authenticated user
		User user = userRepo.findByUserName(authentication.getName());
		
		if(user == null) throw new UserNotFoundException("Could not find user");
		
		return user;
	}
	
	
	
	
}
