package com.hms.jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * 
	Provides custom handling for unauthorized requests, typically when authenitcation
	is required but not supplied or valid.
	when an unauthorized request is detected, it logs the error and returns a json response with an
	error message, status code, and the path attempted.
 */

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
    	//log error
        logger.error("Unauthorized error: {}", authException.getMessage());
        System.out.println(authException);

        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);//set response content type
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//set response status code to 401 because it lack valid credentials

        final Map<String, Object> body = new HashMap<>(); //create map object for the error body
        
        //detailed information about the error
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED); //add error status to the error body
        body.put("error", "Unauthorized"); //actual error
        body.put("message", authException.getMessage()); //error message
        body.put("path", request.getServletPath()); //error path

        final ObjectMapper mapper = new ObjectMapper(); //create object mapper instance
        //convert the error body to json format
        mapper.writeValue(response.getOutputStream(), body);
    }

}
