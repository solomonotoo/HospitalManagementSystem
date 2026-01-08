package com.hms.ps.service;

import org.springframework.stereotype.Service;

import com.hms.ps.module.Patient;
import com.hms.ps.repository.PatienceRepository;

@Service
public class PatientServiceImpl implements PatientService{
	
	private PatienceRepository patienceRepo;
	
	public PatientServiceImpl(PatienceRepository patienceRepo) {
		this.patienceRepo = patienceRepo;
	}

	@Override
	public Patient createPatient(Patient patient) {
		return patienceRepo.save(patient);
	}

}
