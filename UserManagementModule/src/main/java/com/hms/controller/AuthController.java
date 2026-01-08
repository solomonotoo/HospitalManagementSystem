package com.hms.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.controller.audit.UserSignupAudit;
import com.hms.controller.audit.UserSignupAuditRepository;
import com.hms.exceptions.BadRequestException;
import com.hms.jwt.JwtUtils;
import com.hms.modules.Role;
import com.hms.modules.User;
import com.hms.modules.UserRoles;
import com.hms.repository.RoleRepository;
import com.hms.repository.UserRepository;
import com.hms.security.request.LoginRequest;
import com.hms.security.request.SignupRequest;
import com.hms.security.response.LoginResponse;
import com.hms.security.response.MessageResponse;
import com.hms.security.response.UserAccountInfoResponse;
import com.hms.security.services.UserDetailsImpl;
import com.hms.services.MFATotpService;
//import com.hms.security.response.UserAccountInfoResponse;
import com.hms.services.UserService;
import com.hms.util.AuthUtil;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private JwtUtils jwtUtils;

	// NB don't forget to create a bean for AuthenticationManager in the security
	// configuration
	private AuthenticationManager authenticationManager;

	private UserService userService;

	private UserRepository userRepo;

	private PasswordEncoder passwordEncoder;

	private RoleRepository roleRepo;
	private UserSignupAuditRepository auditRepo;
	
	private AuthUtil authUtil;
	
	private MFATotpService mfaTotpService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UserService userService,
			UserRepository userRepo, PasswordEncoder passwordEncoder, RoleRepository roleRepo,
			UserSignupAuditRepository auditRepo, AuthUtil authUtil,MFATotpService mfaTotpService) {
		super();
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.roleRepo = roleRepo;
		this.auditRepo = auditRepo;
		this.authUtil = authUtil;
		this.mfaTotpService = mfaTotpService;
	}

	// NB signin endpoint always has to be public, no need for authentication or
	// protection like jwt for this endpoint
	// handler method for signin
	@PostMapping("/public/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {// takes LoginRequest object as a
																						// parameter
		Authentication authentication; // create authentication object
		try {
			// authenticate the user using UsernamePasswordAuthenticationToken object, and
			// this
			// object contains jwt with username and password, through the authentication
			// manager
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		} catch (AuthenticationException exception) {
			// catch exceptions that will occur during the authentication. thus error
			// response
			Map<String, Object> map = new HashMap<>(); // create a map object
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}

//	      set the authentication object in the security context holder to establish security context for the session.
		// this will mark the user as authenticated in the spring security context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// create UserDetails object of the authentication principal or object. this
		// contains the user credential, roles or authorities
		// assigned or granted to the user
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		// generate token from username presented in UserDetails by invoking
		// generateTokenFromUsername in JwtUtils.java
		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

		// Collect roles from the UserDetails and convert it to a list
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		// Prepare the response body, now including the JWT token directly in the body
		// which contain the username,roles and jwttoken part
		LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);
		System.out.println("user details :" + roles);
		// Return the response entity with the JWT token included in the response body
		return ResponseEntity.ok(response);
	}

	// handler method for user sign up or user registration
	@PostMapping("/public/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		// check if username already exist in the application database
		if (userRepo.existsByUserName(signupRequest.getUsername())) {
			// return ResponseEntity.badRequest().build();
			return ResponseEntity.badRequest().body(new BadRequestException("Error: usename already exist"));
		}

		if (userRepo.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new BadRequestException("Error: email already exist"));
		}

		// Create new user's account with provided username, email and password,
		// if username and email does not exist in the database
		User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
				passwordEncoder.encode(signupRequest.getPassword()));

		// default signup method
		// user.setSignUpMethod("email");

		// get user role in signup request
		Set<String> userRoleInSignupRequest = signupRequest.getRoles();
		// create role object
		Role role = null;

		// check if user role is empty
		if (userRoleInSignupRequest == null || userRoleInSignupRequest.isEmpty()) {
			// set default role(ROLE_USER) by getting the role name from the database if no
			// user role is provided
			role = roleRepo.findByRoleName(UserRoles.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		} else {
			// iterate over list of roles in the database if role is provided in the signup
			// request
			String roleStr = userRoleInSignupRequest.iterator().next();
			// check if role name is admin
			if (roleStr.equals("admin")) {
				// find role by role name
				role = roleRepo.findByRoleName(UserRoles.ROLE_ADMIN)
						// throws exception if roles does not exist
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			} else if (roleStr.equals("user")) {
				// find role by role name
				role = roleRepo.findByRoleName(UserRoles.ROLE_USER)
						// throws exception if role does not exist
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			}

			// set other user account properties
			user.setAccountNonLocked(true);
			user.setAccountNonExpired(true);
			user.setCredentialsNonExpired(true);
			user.setEnabled(true);
			user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
			user.setAccountExpiryDate(LocalDate.now().plusYears(1));
			user.setTwoFactorEnabled(false);
			// NB signup method can change. other example include google,facebook,github etc
			user.setSignUpMethod("email");
		}

		user.setRoles(Collections.singleton(role));

		userService.createUser(user);

		UserSignupAudit audit = new UserSignupAudit(user.getUserName(), user.getEmail(), LocalDateTime.now(),
				user.getSignUpMethod());
		auditRepo.save(audit);

		// auditService.logUserSignIn(signupRequest.getUsername());
		return ResponseEntity.ok(user);
	}

//	//handler method that get user by email
//		@PostMapping("/check_email")
//		public String checkDuplicateEmail(Long id, String email) {
//			return userService.isEmailUnique(id, email) ? "OK" : "Duplicate";
//		}

	// handler method that get the details of loggedIn user. Thus user account
	// details
	@GetMapping("/user")
	public ResponseEntity<?> userAccountInfo(@AuthenticationPrincipal UserDetails userDetails) {

		// extract the username from the authentication principal
		User user = userService.findByUsername(userDetails.getUsername());

		// get the role from authenticated user and convert it to list of roles
		List<String> roles = userDetails.getAuthorities().stream().map(role -> role.getAuthority())
				.collect(Collectors.toList());

		// create a UserAccountInfoResponse object that contains user account
		// information
//		UserAccountInfoResponse userAccountInfo = new UserAccountInfoResponse(user.getId(), user.getFirstName(),
//				user.getLastName(), user.getMaidenName(), user.getUserName(), user.getEmail(), roles);

		UserAccountInfoResponse userAccountInfo = new UserAccountInfoResponse(user.getId(), user.getUserName(),
				user.getEmail(), user.isAccountNonLocked(), user.isAccountNonExpired(), user.isCredentialsNonExpired(),
				user.isEnabled(), user.getCredentialsExpiryDate(), user.getAccountExpiryDate(),
				user.isTwoFactorEnabled(), roles);

		// return the userAccountInfo as a entity response body
		return ResponseEntity.ok().body(userAccountInfo);
	}

//	@GetMapping("/user")
//	public ResponseEntity<?> userAccountInfo(@AuthenticationPrincipal Object principal) {
//	    if (principal == null) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authenticated user found");
//	    }
//
//	   
//		LOGGER.info("Principal class: {}", principal.getClass().getName());
//
//	    if (principal instanceof UserDetails userDetails) {
//	        User user = userService.findByUsername(userDetails.getUsername());
//	        return ResponseEntity.ok(user);
//	    } else if (principal instanceof OAuth2User oAuth2User) {
//	        String email = (String) oAuth2User.getAttributes().get("email");
//	        User user = userService.findByEmail(email);
//	        return ResponseEntity.ok(user);
//	    }
//
//	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unknown authentication type");
//	}


	// handler method that get the username of the current loggedIn user
	@GetMapping("/username")
	public String currentLoggedInUsername(@AuthenticationPrincipal UserDetails userDetails) {
		return userDetails != null ? userDetails.getUsername() : "";
	}

	// handler method that send forgot password request to the user email
//	@PostMapping("/public/forgot-password")
//	public ResponseEntity<?> forgotPasswordRequest(@RequestParam String email){
//		
//		try {
//			userService.generatePasswordResetToken(email);
//			return ResponseEntity.ok(new MessageResponse("Password reset email sent!"));
//		} catch (Exception e) {
//			//e.printStackTrace();
//			return ResponseEntity.ok(new MessageResponse("Error sending password reset email"));
//		}
//	}

	@PostMapping("/public/forgot-password")
	public ResponseEntity<?> forgotPasswordRequest(@RequestParam String email) {
		User user = userService.generatePasswordResetToken(email);
		// return ResponseEntity.ok(new MessageResponse("Password reset email sent!"));
		return ResponseEntity
				.ok(new MessageResponse("Password reset email has been sent successfully to " + user.getEmail() + "!"));
	}

	// NB when testing the application in postman ensure that Query params names
	// match with what is used here
	// e.g the parameter password used should be the same as the one used for Query
	// params in postman
	@PostMapping("/public/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam String passwordResetToken, @RequestParam String newPassword) {
		userService.resetPassword(passwordResetToken, newPassword);

		return ResponseEntity.ok(new MessageResponse("Password Reset Successful!"));
	}

	
	//########## FOR 2FA AUTHENTICATION ENDPOINT ############
	
	//handler method that enabled and disabled 2fa authentication mode
	@PostMapping("/toggle-2fa")
	public ResponseEntity<String> toggle2FA() {
		//get the id of the authenticated user
		Long userId = authUtil.getLoggedInUserID();
		 
		//retrieve user and check the current 2FA status
		User user = userService.getUserById(userId);
		boolean is2FaEnabled = user.isTwoFactorEnabled();
			
		if(!is2FaEnabled) {
			 //retrieve 2fa secret key associated with authenticated user
			 GoogleAuthenticatorKey secretKey = userService.generate2FASecret(userId);
			 
			 //retrieve the QR Code associated with the authenticated user
			 String qrCodeUrl = mfaTotpService.getQRCodeUrl(secretKey, userService.getUserId(userId).getUserName());
			 
			 return ResponseEntity.ok(qrCodeUrl);
		}else {
			userService.enable2FA(userId, false);
			return ResponseEntity.ok("2FA Disabled");
		}
		 
	}
	
	
	
	//handler method that retrieve the MFA status from the database when the Enable MFA button
	//clicked in the frontend
	@GetMapping("/user/mfa-status")
	public ResponseEntity<?> getMFAStatus(){
		//get the username of the authenticated user
		User user = authUtil.getloggedInUsername();
		
		if(user != null ) {
			return ResponseEntity.ok().body(Map.of("is2faEnabled", user.isTwoFactorEnabled()));
		}else {
			return ResponseEntity.ok(new UsernameNotFoundException("User not found"));
		}
	}
	
	
	//handler method that verify MFA code
	@PostMapping("/verify-mfa-code")
	public ResponseEntity<String> verifyMFACode(@RequestParam int code){
		//retrieve ID of the authenticated user
		Long userId = authUtil.getLoggedInUserID();
		
		//validate the MFA code
		boolean mfaCodeValid = userService.validate2FaCode(userId, code);
		
		//enable MFA if mfa code is valid
		if(mfaCodeValid) {
			userService.enable2FA(userId, true);
			return ResponseEntity.ok("MFA Code Verified");
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid MFA Code");
		}
	}
	
	//handler method that verify MFA login. this method comes after login and it has to public
	//if not it will not work
	@PostMapping("/public/verify-mfa-login")
	public ResponseEntity<?> verifyMFALogin(@RequestParam int code, @RequestParam String jwtToken){
		//grab the username from jwt token
		String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
		
		//retrieve the username from the database using the username from the jwt token
		User user = userService.findByUsername(username);
		
		//validate the MFA Code associated with the username in the database
		boolean isMFACodeValid = userService.validate2FaCode(user.getId(), code);
		
		if(isMFACodeValid) {
			return ResponseEntity.ok("MFA Code Verified");
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid MFA code");
		}
	}
	
	//########## END OF 2FA AUTHENTICATION ENDPOINT ############
	
	
}
