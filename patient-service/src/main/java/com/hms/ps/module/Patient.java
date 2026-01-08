package com.hms.ps.module;

import com.hms.ps.Gender;
import com.hms.ps.MaritalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="patients")
public class Patient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "maiden_name")
	private String maidenName;
	
	private String email;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	
	@Column(name = "marital_status")
	@Enumerated(EnumType.STRING)
	private MaritalStatus maritalStatus;
	
	private String address;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "social_security_number")
	private String socialSecurityNumber;
	

}
