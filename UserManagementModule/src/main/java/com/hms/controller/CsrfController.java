package com.hms.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;


//CSRF Token controller
@RestController
public class CsrfController {

	//handler method that retrieve the csrf token value
	@GetMapping("/api/csrf-token")
	public CsrfToken csrfToken(HttpServletRequest request) {
		// return the attribute of the csrf token
		return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	}
	
}
