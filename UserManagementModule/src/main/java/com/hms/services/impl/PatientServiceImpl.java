package com.hms.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hms.modules.Patient;
import com.hms.security.response.PatienceRepository;
import com.hms.services.PatientService;

@Service
public class PatientServiceImpl implements PatientService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);
	
	private PatienceRepository patientRepo;
	
	public PatientServiceImpl(PatienceRepository patientRepo) {
		this.patientRepo = patientRepo;
	}


	@Override
	public Patient createPatient(Patient patient) {
		LOGGER.info("Createing patient: {}", patient);
		Patient savedPatient = patientRepo.save(patient);
		LOGGER.info("Patient created succesfully: {}", savedPatient);
		return savedPatient;
	}


	@Override
	public List<Patient>  getAllPatients() {
		return patientRepo.findAll();
	}

}
