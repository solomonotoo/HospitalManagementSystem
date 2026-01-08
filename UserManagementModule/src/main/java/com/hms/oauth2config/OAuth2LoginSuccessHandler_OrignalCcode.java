//package com.hms.oauth2config;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import com.hms.jwt.JwtUtils;
//import com.hms.modules.Role;
//import com.hms.modules.User;
//import com.hms.modules.UserRoles;
//import com.hms.repository.RoleRepository;
//import com.hms.security.services.UserDetailsImpl;
//import com.hms.services.UserService;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//
////NB The main purpose of this class is to handle all login that involves Oauth2
//
//@Component
//public class OAuth2LoginSuccessHandler_OrignalCcode extends SavedRequestAwareAuthenticationSuccessHandler{
//
//	private final UserService userService;
//	
//	private final JwtUtils jwtUtils;
//	
//	private RoleRepository roleRepo;
//	
//	@Value("${frontend.url}")
//	private String frontendUrl;
//	
//	//stores the username for the oauth2 login
//	private String username;
//	
//	// represent the login key
//	private String idAttributeKey;
//
//	public OAuth2LoginSuccessHandler_OrignalCcode(UserService userService, JwtUtils jwtUtils, RoleRepository roleRepo) {
//		super();
//		this.userService = userService;
//		this.jwtUtils = jwtUtils;
//		this.roleRepo = roleRepo;
//	}
//
//	
//	//method that define the logic for successful oauth2 authentication
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws ServletException, IOException {
//		
//		//cast the Authentication object which represent the token or principal for authentication to OAuth2AuthenticationToken
//		OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
//		
//		//check the registration ID  of the authorized client.Thus oauth2 authentication provider ID eg github,google,facebook,linkedin
//		if("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()) || 
//				"google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
//			
//			//extract the principal from the authentication object and cast it to DefaultOAuth2User
//			//thus the username or id of the person who authorizing the oauth2 login
//			DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
//			
//			
//			//extract all the user attribute from the defaultOAuth2User which represent the principal into a map
//			Map<String, Object> attributes = principal.getAttributes();
//			
//			//extract the username and email from the attributes or give a default  empty string value
//			String email = attributes.getOrDefault("email", "").toString();
//			String name = attributes.getOrDefault("name", "").toString();
//			
//			// NB github and google have different attribute for oauth2 login and the code
//			//below handles each case
//			
//			//check the type of authorized client registration id.this is for github
//			if("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
//				// with github username is stored in the login key and that is what we are
//				// extracting
//				username = attributes.getOrDefault("login", "").toString();
//				
//				// attribute key for login
//				idAttributeKey = "id";
//				
//			} else if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) { //for google
//				// for google username is extracted from the email address
//				username = email.split("@")[0];
//				
//				// attribute key for login
//				idAttributeKey = "sub";
//			}
//			
//			/*
//			 * twitter is username , idAttributeKey is id facebook extract it from the
//			 * email, idAttributeKey is id linkedIn, extract it from the email,
//			 * idAttributeKey is sub
//			 */
//			
//			// for debug purpose
//			System.out.println("HELLO OAUTH: " + email + " : " + name + " : " + username);
//
//			//retrieve user email exist in the database
//			User user = userService.findByEmail(email);
//			
//			
//			//If a value is present, performs the given action with the value,otherwise 
//			//performs the given empty-based action
//			//Thus if the user email is present update the security context with the existing user details
//			if(user != null){
//				List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//	                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
//	                    .collect(Collectors.toList());
//				
//				
//				//create a new DefaultOAuth2User object
//				DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
//						//add the list of user roles,attributes and idAttributeKey to the DefaultOAuth2User object
//						authorities, attributes, idAttributeKey);
//				
//				//create an Authentication object for the authenticated user using the existing user role and 
//				//and OAuth2AuthenticationToken
//				Authentication securityAuth = new OAuth2AuthenticationToken(oAuth2User,
//						authorities,
//						oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
//				
//				//update the security context holder with the authenticated user
//				SecurityContextHolder.getContext().setAuthentication(securityAuth);
//			}else {
//					//create a new user object if the does not exist in the database
//					User newUser = new User();
//					
//					//assign a default role("ROLE_USER") to the user 
//					Optional<Role> userRole = roleRepo.findByRoleName(UserRoles.ROLE_USER);
//					
//					//check if the default role exist in the database
//					if(userRole.isPresent()) {
//						//set the existing role
//						newUser.setRolesFromJson(userRole.get());
//						
//					}else {
//						//throws exception if the role does not exist in the database
//						throw new RuntimeException("Default Role Not Found");
//					}
//					
//					//set email, userName and signUpMethod for the new user
//					newUser.setEmail(email);
//					newUser.setUserName(username);
//					newUser.setSignUpMethod(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
//					
//					//create a new user
//					userService.registerUser(newUser);
//					
//					//create a list of user roles
//					List<SimpleGrantedAuthority> authorities = newUser.getRoles().stream()
//		                    .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
//		                    .collect(Collectors.toList());
//					
//					//create DefaultOAuth2User object with the role of the newly created user along with attributes and 
//					//idAttributeKey
//					DefaultOAuth2User oauthUser = new DefaultOAuth2User(
//							authorities, attributes,
//							idAttributeKey);
//					
//					//create the authentication object
//					Authentication securityAuth = new OAuth2AuthenticationToken(oauthUser,
//							authorities,
//							oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
//					
//					//update the security context with the authentication object
//					SecurityContextHolder.getContext().setAuthentication(securityAuth);
//				};
//		}
//		
//		//set the default target url to true. this ensures page is always redirected to the same default URL.
//		this.setAlwaysUseDefaultTargetUrl(true);
//		
//		// JWT TOKEN LOGIC
//		// get the oauth2 principle and cast to DefaultOAuth2User. thus the username or
//		// id of the person who authorizing the oauth2 login
//		DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
//		
//		//Extract all the attribute from the oAuth2User into a map
//		Map<String, Object> attributes = oAuth2User.getAttributes();
//		
//		//extract the necessary attribute for oauth2 login which include the email
//		String email = (String) attributes.get("email");
//		
//		System.out.println("OAuth2LoginSuccessHandler: " + username + " : " + email);
//		
//		//create a set of authorities or roles from DefaultOAuth2User thus github,google,facebook etc
//		Set<SimpleGrantedAuthority> authoritiesFromDefaultOAuth2User = new HashSet<>(oAuth2User.getAuthorities().stream()
//				.map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
//				.collect(Collectors.toList()));
//		
//		//fetch user roles from the database. this is necessary because we will need the role of the 
//		//user (eg AppRole.ROLE_USER) from the database to the frontend to determine if the user is an admin or not. the set of roles from the
//		//oauth2 does not include such role
//		User user = userService.findByEmail(email);
//		
//		List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//				.map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).collect(Collectors.toList());
//		
//		 //add the user roles from the database to the oauth2 roles eg. github roles
//		authoritiesFromDefaultOAuth2User.addAll(authorities);
//		
//		// Create UserDetailsImpl instance or object which include the username,email, role from oauth2(eg. github) and role
//        //from database. this will be used to generate the jwt token
//		UserDetailsImpl userDetails = new UserDetailsImpl(null, username, email, null, false, authoritiesFromDefaultOAuth2User);
//		
//		//Generate the Jwt Token from the userDetails
//		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
//		
//		// Redirect to the frontend with the JWT token. Build or create the target url where the user will be directed to 
//		//if authentication is successful. the jwt token is also appended to target url as a query parameter.
//		//NB "/oauth2/redirect" endpoint has to be handled because that's where users are redirected to when authentication
//		//is successful
//		String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
//				.queryParam("token", jwtToken).build().toUriString();
//		
//		this.setDefaultTargetUrl(targetUrl);
//		
//		// TODO Auto-generated method stub
//		super.onAuthenticationSuccess(request, response, authentication);
//	}
//	
//	
//}
