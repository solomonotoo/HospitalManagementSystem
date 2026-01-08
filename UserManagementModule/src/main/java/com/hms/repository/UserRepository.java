package com.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hms.modules.User;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>{

	/*NB :email refers to the parameter value in @Param("email") */
	//repository method to get user email
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User findByEmail(@Param("email") String email);
	
	//public Optional<User> findByUserName(String username);
	public User findByUserName(String username);
	
	//repository method used to check the existence of  user before deletion
	public Long countById(Long id);

	//repository method that check if username already exist in the database
	public boolean existsByUserName(String username);
	
	//repository method that check if email already exist in the database
	public boolean existsByEmail(String email);

	//repository method that update 2fa status
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.isTwoFactorEnabled = :enabled WHERE u.id = :userId")
	public void update2FaStatus(@Param("userId") Long userId, @Param("enabled") boolean enabled);
}
