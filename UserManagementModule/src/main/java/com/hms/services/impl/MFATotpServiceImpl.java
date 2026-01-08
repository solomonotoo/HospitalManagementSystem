package com.hms.services.impl;

import org.springframework.stereotype.Service;

import com.hms.services.MFATotpService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;


@Service
public class MFATotpServiceImpl implements MFATotpService{

	//variable to store 2fa generated code
	private final GoogleAuthenticator gAuth;

	public MFATotpServiceImpl(GoogleAuthenticator gAuth) {
		super();
		this.gAuth = gAuth;
	}
	
	public MFATotpServiceImpl() {
		super();
		this.gAuth = new GoogleAuthenticator();
	}
	
	//method that generate secret key for 2fa
	@Override
	public GoogleAuthenticatorKey generateKey() {
		//generate credential for 2fa and this contains:
		//1. secret key
		//2.validation code
		//3.3.A list of scratch codes
		return gAuth.createCredentials();
	}
	
	//method that generate url for QRCode.This can be fed to the frontend app or
	//google authenticator app
	@Override
	public String getQRCodeUrl(GoogleAuthenticatorKey secret, String username) {
		//generate the QRCode using the application name, username and secret
		//NB GoogleAuthenticatorQRGenerator -> This class provides helper methods to create a QR code containing theprovided credential. 
		//The generated QR code can be fed to the GoogleAuthenticator application so that it can 
		//configure itself with the datacontained therein.
		return GoogleAuthenticatorQRGenerator.getOtpAuthURL("EccLink Hospital Management System", username, secret);
	}
	
	//method that verify the code the user will enter
	@Override
	public boolean verifyCode(String secret, int code) {
		return gAuth.authorize(secret, code);
	}
	
}
