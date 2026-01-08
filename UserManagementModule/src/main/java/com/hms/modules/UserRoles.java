package com.hms.modules;

public enum UserRoles {

	
	ROLE_ADMIN("Administrator with full access to the system"),
	ROLE_USER("User with limited access to the system"),
	ROLE_NURSE("User with limited access to the patient medical records"),
	ROLE_PHARMACIST("User with full access to patient medication"),
	ROLE_PATIENT("User with full access to his or her medical records"),
	ROLE_BILLING_STAFF("Staff with full access to viewing patient bills and billing patients"),
	ROLE_DOCTOR("User with full  access to the patient records"),
	ROLE_RECEPTIONIST("User with  access to the OPD");

	
	private final String description;

	private UserRoles(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	
}
