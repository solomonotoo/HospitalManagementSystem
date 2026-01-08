package com.hms.controller.audit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSignupAuditRepository extends JpaRepository<UserSignupAudit, Long>{

}
