package com.hms.services.impl;

import org.springframework.stereotype.Service;

import com.hms.modules.ContactUs;
import com.hms.repository.ContactRepository;
import com.hms.services.ContactUsService;

@Service
public class ContactUsServiceImpl implements ContactUsService{

	private ContactRepository contactRepo;
	
	public ContactUsServiceImpl (ContactRepository contactRepo) {
		this.contactRepo = contactRepo;
	}
	
	
	@Override
	public ContactUs saveMessage(ContactUs contact) {
		
		return contactRepo.save(contact);
	}

}
