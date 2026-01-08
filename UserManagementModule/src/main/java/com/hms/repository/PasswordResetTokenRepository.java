package com.hms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hms.modules.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

	@Query("SELECT t FROM PasswordResetToken t where t.token = ?1 ")
	Optional<PasswordResetToken> findByToken(String token);

}
