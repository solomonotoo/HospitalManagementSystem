package com.hms.ps.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.ps.module.Patient;
import com.hms.ps.service.PatientService;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientRestApiController {
	
	private PatientService patientService;

	public PatientRestApiController(PatientService patientService) {
		super();
		this.patientService = patientService;
	}
	
	public Patient createPatient(Patient patient) {
		
		return patientService.createPatient(patient);
	}

}
