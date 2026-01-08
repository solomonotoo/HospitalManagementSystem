package com.hms.ps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.ps.module.Patient;

public interface PatienceRepository extends JpaRepository<Patient, Long>{

}
