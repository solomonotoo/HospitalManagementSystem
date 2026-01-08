package com.hms.services;

import java.util.List;

import com.hms.modules.Patient;

public interface PatientService {

	public Patient createPatient(Patient patient);

	public List<Patient> getAllPatients();
}
