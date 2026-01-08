package com.hms.security.response;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.modules.Patient;



public interface PatienceRepository extends JpaRepository<Patient, Long>{

}
