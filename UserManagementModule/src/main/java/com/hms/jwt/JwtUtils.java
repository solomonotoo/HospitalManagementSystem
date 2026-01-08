package com.hms.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.hms.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {

	//logger instance
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtSecret}") //value is in application.profile
    private String jwtSecret; //jwt secret

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs; //jwt expiration time

    //extract jwt token from request header
    public String getJwtFromHeader(HttpServletRequest request) {
    	//get authorization header value. Thus jwt value presented in the request header
        String bearerToken = request.getHeader("Authorization");
        
        //log the header values for debugging purpose
        logger.debug("Authorization Header: {}", bearerToken);
        
        //check if the header is not null and starts with the prefix (Bearer ) 
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove Bearer prefix
        }
        return null;
    }
    
    //generate jwt token from the username  //NB you can add roles or anything that you want to share with the user 
    public String generateTokenFromUsername(UserDetailsImpl userDetails) {//pass the user details implementation class as a parameter
        String username = userDetails.getUsername();//get the username from the user details object
        String roles = userDetails.getAuthorities().toString();
     // Extract roles from UserDetails and convert them to a list of strings
//        List<String> roles = userDetails.getAuthorities()
//                                        .stream()
//                                        .map(GrantedAuthority::getAuthority)
//                                        .collect(Collectors.toList());

        return Jwts.builder()//make use of jwt builder which start building the jwt token
                .subject(username)//set subject of the username of the jwt
                .claim("roles", roles)
                
                //for MFA verification during login. NB ensure the string "is2FaEnabled" matches the string used in the frontend login else
                //it will create problem 
                .claim("is2faEnabled", userDetails.is2faEnabled())
                .issuedAt(new Date())//set the issued date
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))//set the expiration time by adding jwtExpiration in milliseconds
                .signWith(key())//sign it with a key (secret key)
                .compact();//build and return jwt as a compact string
    }
    
  //extract the username for the generated jwt token
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key()) //verify the secret key
                .build().parseSignedClaims(token) //Parses the  token and signed Claims of JWS.
                .getPayload()//payload contains the data that is to be transmitted securely
                .getSubject();//the subject contains the username
    }
    
 // Extract all claims from token
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                   .verifyWith((SecretKey) key())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    
  //generate the secret key
    private Key key() {
    	//generate key base on base64 encoded string
        return Keys.hmacShaKeyFor(
        		Decoders.BASE64.decode(jwtSecret) //decode the key
        		);
    }
    
    //validate the jwt token
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            //validate the jwt token using the secret key
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true; //returns true if token is valid
            
            //catch all exception that may occur
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false; //returns false if token is not valid
    }
}
