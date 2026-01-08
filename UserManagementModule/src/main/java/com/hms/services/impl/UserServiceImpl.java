package com.hms.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.exceptions.PasswordTokenInvalidException;
import com.hms.exceptions.UserNotFoundException;
import com.hms.modules.PasswordResetToken;
import com.hms.modules.Role;
import com.hms.modules.User;
import com.hms.modules.UserDTO;
import com.hms.modules.UserRoles;
import com.hms.repository.PasswordResetTokenRepository;
import com.hms.repository.RoleRepository;
import com.hms.repository.UserRepository;
import com.hms.services.MFATotpService;
import com.hms.services.UserService;
//import com.hms.util.EmailService;
import com.hms.util.EmailService;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

@Service
public class UserServiceImpl implements UserService {

	@Value("${frontend.url}")
	private String frontendURL;
	
	private UserRepository userRepo;
	private RoleRepository roleRepo;

	private PasswordEncoder passwordEncoder;

	private PasswordResetTokenRepository passwordTokenRepo;
	private ModelMapper modelMapper;
	
	//@Autowired
	private EmailService emailService;
	
	private MFATotpService mfaTotpService;

	

	public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder,
			PasswordResetTokenRepository passwordTokenRepo, ModelMapper modelMapper, EmailService emailService,
			MFATotpService mfaTotpService) {
		super();
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
		this.passwordTokenRepo = passwordTokenRepo;
		this.modelMapper = modelMapper;
		this.emailService = emailService;
		this.mfaTotpService = mfaTotpService;
	}

	@Override
	public List<User> listUsers() {
		return userRepo.findAll();
	}

//	@Override
//	public User createUser(User user) {
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		
//		return userRepo.save(user);
//	}

//	@Override
//	public User createUser(User user) {
//		// Convert role names (if provided as strings) to Role objects
//		Set<Role> userRoles = new HashSet<>();
//		for (Role role : user.getRoles()) {
//			if (role.getRoleName() != null) {
//				Role existingRole = roleRepo.findByRoleName(role.getRoleName())
//						.orElseThrow(() -> new IllegalArgumentException("Invalid role name: " + role.getRoleName()));
//				userRoles.add(existingRole);
//			}
//		}
//		user.setRoles(userRoles);
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		return userRepo.save(user); // Save the user with the associated roles
//	}
	
	
	
	@Override
	public User createUser(User user) {
		
		return userRepo.save(user); // Save the user with the associated roles
	}

	@Override
	public User getUserById(Long userId) {

		try {
			return userRepo.findById(userId).get();

		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find user ID: " + userId);
		}
	}

	@Override
	public UserDTO getUserId(Long userId) {

		try {
			User user = userRepo.findById(userId).get();
			return entityToDTO(user);
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find user ID: " + userId);
		}
	}

	private UserDTO entityToDTO(User user) {
		return modelMapper.map(user, UserDTO.class);
	}

//	@Override
//	public User updateUser(User user, Long userId) {
//		User existingUser = getUserId(userId)
//				.firstName(user.getFirstName())
//				.maidenName(user.getMaidenName())
//				.lastName(user.getLastName())
//				.dateOfBirth(user.getDateOfBirth())
//				.phoneNumber(user.getPhoneNumer());
//		
//		return userRepo.save(existingUser);
//	}

	@Override
	public void deleteUser(Long userId) {

		Long countById = userRepo.countById(userId);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find user with ID: " + userId);
		}

		userRepo.deleteById(countById);
	}

	@Override
	public boolean isEmailUnique(Long id, String email) {
		User getUserEmail = userRepo.findByEmail(email);
		if (getUserEmail == null)
			return true;

		boolean isCreatingNew = id == null;

		if (isCreatingNew) {
			// if email is not null meaning the email exist in the db
			// therefore the email is not unique. New email must be provided
			if (getUserEmail != null) {
				return false;
			}
		} else {
			// if the ID of the user find by email is different from the ID
			// of the user being edited then email is not unique
			if (getUserEmail.getId() != id) {
				return false;
			}

		}

		return true;

	}

	@Override
	public User findByUsername(String username) {
		try {
			return userRepo.findByUserName(username);
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Cound Not find username:" + username);
		}
	}

	@Override
	public User updateAccountLockStatus(Long userId, boolean lock) {
		//retrieve user id from the db
		User user = getUserById(userId);
		//update the user account locked status
		user.setAccountNonLocked(!lock);
		return userRepo.save(user);

	}

	@Override
	public User updateAccountNonExpired(Long userId, boolean expire) {
		//retrieve user id from the db
		User user = getUserById(userId);
		user.setAccountNonExpired(!expire);
		return userRepo.save(user);
		
	}

	@Override
	public User updateCrendentialsExpiryStatus(Long userId, boolean expire) {
		//retrieve user id from the db
		User user = getUserById(userId);
		user.setCredentialsNonExpired(!expire);
		return userRepo.save(user);
		
	}

	@Override
	public User updateAccountEnabledStatus(Long userId, boolean enabled) {
		//retrieve user id from the db
		User user = getUserById(userId);
		user.setEnabled(!enabled);
		return userRepo.save(user);
		
	}

	@Override
	public List<Role> getAllRoles() {
		return roleRepo.findAll();
		
	}

	//for a single role
//	@Override
//	public User updateUserRole(Long userId, String roleName) {
//		// retrieve the user id
//		User user = getUserById(userId);
//		
//		//get the current role of the user
//		UserRoles userRoles = UserRoles.valueOf(roleName);
//		
//		//Get the role object
//		Role role = roleRepo.findByRoleName(userRoles)
//				//throws exception if role name not found
//				.orElseThrow(()-> new RuntimeException("Role not found"));
//		//user.getRoles().clear();  // Remove previous roles (optional, if replacing roles)
//		user.getRoles().add(role);
//		return userRepo.save(user);
//	}
	
	//for multiple roles
	@Override
	public User updateUserRole(Long userId, List<String> roleNames) {
		// retrieve the user id
		User user = getUserById(userId);
		// Retrieve the roles from the database
	    Set<Role> newRoles = roleNames.stream()
	    		.map(roleName -> {
	                try {
	                    UserRoles enumRole = UserRoles.valueOf(roleName.trim().toUpperCase()); // Ensure case matches
	                    return roleRepo.findByRoleName(enumRole)
	                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
	                } catch (IllegalArgumentException e) {
	                    throw new RuntimeException("Invalid role name: " + roleName);
	                }
	            })
	        .collect(Collectors.toSet());

	    // Replace the existing roles with the new ones
	    user.setRoles(newRoles);
		return userRepo.save(user);
	}
	

	@Override
	public User updateUserPassword(Long userId, String password) {
		try {
			User user = getUserById(userId);
			user.setPassword(passwordEncoder.encode(password));
			return userRepo.save(user);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update user password");
		}
	}

	
	//method that generate password reset token
	@Override
	public User generatePasswordResetToken(String email) {
		//retrieve user email from the database
		User user = userRepo.findByEmail(email);
		
		//throws exception if no user is found
		if (user == null) {
	        throw new UserNotFoundException("User with email " + email + " not found.");
	    }
		
		//generate a random token
		String token = UUID.randomUUID().toString();
		
		//create an expiry date for the token
		Instant tokenExpiryDate  = Instant.now().plus(24, ChronoUnit.HOURS);
		
		//create password reset token using the expiry date and the random generated string
		PasswordResetToken resetToken = new PasswordResetToken(token,tokenExpiryDate,user);
		
		//save the token into the database
		passwordTokenRepo.save(resetToken);
		
		//url for password reset token and this will be send to the user email
		String tokenResetUrl = frontendURL + "/reset-password/?passwordResetToken=" + token;
		
		//send email to the user
		emailService.sendPasswordResetEmail(user.getEmail(), tokenResetUrl);
		
		return user;
	}
	
	//service method that reset password
	@Override
	public User resetPassword(String passwordResetToken, String newPassword) {
		
		System.out.println("password reset token : " + passwordTokenRepo.findByToken(passwordResetToken));
		
		//retrieve password reset token from the database
		PasswordResetToken resetToken = passwordTokenRepo.findByToken(passwordResetToken)
				.orElseThrow(()-> new PasswordTokenInvalidException("Invalid Password Reset Token"));
		
		//check if the token is used
		if(resetToken.istokenUsed()) {
			throw new PasswordTokenInvalidException("Password Reset Token Has Already Been Used!");
		}
		
		//check token has expired
		if(resetToken.getTokenExpiryDate().isBefore(Instant.now())) {
			throw new PasswordTokenInvalidException("Password Reset Token Has Expired!");
		}
		
		
		//get the user whose password is being reset
		User user = resetToken.getUser();
		
		//set the new password
		user.setPassword(passwordEncoder.encode(newPassword));
		System.out.println("New user password: " + newPassword);
		//save the new password
		userRepo.save(user);
		
		//update the used to true after password has been updated or reset
		resetToken.setIstokenUsed(true);
		
		passwordTokenRepo.save(resetToken);
		System.out.println("saved password  : " + passwordTokenRepo.save(resetToken));
		return user;
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepo.findByEmail(email);
	}

	//method that register a new user using the oauth2 if the user does not exist
	@Override
	public User registerUser(User newUser) {
		
		//encode the password
		if(newUser.getPassword() != null) {
			newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		}
		return userRepo.save(newUser);
		
	}
	
	//######for 2 factor authentication #####
	//1. method that generate the mfa secret key for the authenticated user
	@Override
	public GoogleAuthenticatorKey generate2FASecret(Long userId) {
		//retrieve user id from the database
		User user = userRepo.findById(userId).get();
		
		//generate 2fa secret
		GoogleAuthenticatorKey key = mfaTotpService.generateKey();
		
		//set the 2fa key to be saved against the authenticated user
		user.setTwoFactorSecret(key.getKey());
		
		//save the user
		userRepo.save(user);
		
		//return the key
		return key;
		
		
	}
	
	
	//2. method that validate the 2fa code that the user will type
	@Override
	public boolean validate2FaCode(Long userId,int code) {
		
		//retrieve user id from the database
		User user = userRepo.findById(userId).get();
		
		return mfaTotpService.verifyCode(user.getTwoFactorSecret(), code);
	}
	
//	//3. method that enabled and disabled 2fa authentication
//	public void enabled2Fa(Long userId) {
//		User user = userRepo.findById(userId).get();
//		
//		user.setEnabled(true);
//		
//		return userRepo.save(user);
//	}
//	
//	//3. method that enabled and disabled 2fa authentication
//		public void disabled2Fa(Long userId) {
//			User user = userRepo.findById(userId).get();
//			
//			user.setEnabled(false);
//			
//			return userRepo.save(user);
//		}
	
	@Override
	public void enable2FA(Long userId, boolean enabled) {
		 userRepo.update2FaStatus(userId,enabled);
	}
	
	
	
	
	
	
	
	
	
	
	
	//####### end of 2 factor authentication ########
}
