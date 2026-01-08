package com.hms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hms.modules.Patient;
import com.hms.services.PatientService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/patients")
public class PatientRestApiController {
	
	private PatientService patientService;

	public PatientRestApiController(PatientService patientService) {
		super();
		this.patientService = patientService;
	}
	
	@GetMapping
	public ResponseEntity<?> listPatients(){
		List<Patient> patients = patientService.getAllPatients();
		return ResponseEntity.ok(patients);
	}
	
	 @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

}
