package com.hms.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.modules.ContactUs;
import com.hms.services.ContactUsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/contactus")
public class ContactUsRestApiController {

	private ContactUsService contactUsService;
	
	public ContactUsRestApiController(ContactUsService contactUsService) {
		super();
		this.contactUsService = contactUsService;
	}

	@PostMapping
	public ResponseEntity<ContactUs> createContactUs(@RequestBody @Valid ContactUs contactUs){
		ContactUs contact = contactUsService.saveMessage(contactUs);
		URI uri = URI.create("/v1/contactus");
		
		return ResponseEntity.created(uri).body(contact);
	}
}
