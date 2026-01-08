package com.hms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//NB this is a helper class and it contains all the code for sending email.

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	
	//method that send forgot/reset password request message 
	//this method takes two parameters
	//1. reset url 2.email of the sender 
	
	public void sendPasswordResetEmail(String to, String resetUrl) {
		//create instance object of SimpleMailMessage
		SimpleMailMessage message = new SimpleMailMessage();
		
		//where the mail is being send to
		message.setTo(to);
		
		
		//subject for the email
		message.setSubject("Password Reset Request");
		
		//actual email messag
		message.setText("Click the link to reset your password: " + resetUrl);
		
		//send the message
		mailSender.send(message);
		
		
	}
	
	
	
}
