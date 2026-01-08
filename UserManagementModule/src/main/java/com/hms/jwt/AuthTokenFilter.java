package com.hms.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hms.security.services.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


/* this class will intercept every request  for validation of jwt and it relies heavily on JwtUtils.java
 * Filters incoming requests to check for a valid jwt in the request header, settings the 
	authentication context if the token is valid.
	Extracts jwt from request header, validates it, and configures the spring security 
	context with user details if the token is valid
 */

//Authtoken filter executes once per every request

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils; //inject jwtUtils

    @Autowired
    private UserDetailsServiceImpl userDetailsService; //inject UserDetailsServiceImpl

    public AuthTokenFilter() {}
    
//    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
//		super();
//		this.jwtUtils = jwtUtils;
//		this.userDetailsService = userDetailsService;
//	}

	//create logger instance
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    //override doFilterInternal method
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        
        try {
        	//extract the jwt from the request using the parseJwt method
            String jwt = parseJwt(request);
            
            //check if jwt is not null and validate the jwt which is expect to return a true value
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            	
            	//extract the username from the jwt
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

             // Extract roles from JWT token
//                Claims claims = jwtUtils.getAllClaimsFromToken(jwt);
//                List<String> roles = claims.get("roles", List.class);  // Extract roles claim
                	
                String roles = jwtUtils.getAllClaimsFromToken(jwt).toString();
                
                // Convert roles from String to GrantedAuthority
//                List<GrantedAuthority> authorities = roles.stream()
//                                                          .map(SimpleGrantedAuthority::new)
//                                                          .collect(Collectors.toList());

             
                //Claims claims = jwtUtils.getAllClaimsFromToken(jwt);
                
                //load the user details using the extracted username from jwt
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //create or generate a usernamePasswordAuthentication token object from the userDetails which contain username and password
                //An org.springframework.security.core.Authentication implementation that is designed for 
                //simple presentation of a username and password. 
               // The principal and credentials should be set with an Object that provides the respective
                //property via its Object.toString() method. The simplest such Object to use is String.
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                        		roles,
                                userDetails.getAuthorities()); //pass in the role associated with the username
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities()); //log the user role

                //set and build the details for the authentication object from the request
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //update the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
        	//catch error
            logger.error("Cannot set user authentication: {}", e);
        }

        //call the doFilter from filterChain to continue the execution of other filters
        filterChain.doFilter(request, response);
    }

    //private method that get or extract jwt from the request header
    private String parseJwt(HttpServletRequest request) {
    	//invoke getJwtFromHeader which extract jwt header
        String jwt = jwtUtils.getJwtFromHeader(request);
        
        //log the extract jwt
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;//return jwt
    }
}
