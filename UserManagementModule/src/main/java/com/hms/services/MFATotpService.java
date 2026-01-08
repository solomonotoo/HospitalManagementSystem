package com.hms.services;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public interface MFATotpService {

	GoogleAuthenticatorKey generateKey();

	String getQRCodeUrl(GoogleAuthenticatorKey secret, String username);

	boolean verifyCode(String secret, int code);

}
