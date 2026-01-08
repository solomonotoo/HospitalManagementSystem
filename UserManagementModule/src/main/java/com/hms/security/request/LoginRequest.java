package com.hms.security.request;


//this class will define the  format a request should be sent for login.
//thus how username and password will be sent for login

//NB this is a dto and separated because if in the feature you want to add more field like role(which will a drop down list in the UI) in the login request
//you can do so easily

public class LoginRequest {

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
