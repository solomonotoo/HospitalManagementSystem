package com.hms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.hms.modules.Role;
import com.hms.modules.UserRoles;

@Transactional
public interface RoleRepository extends JpaRepository<Role,Integer> {

//	@Query("SELECT r.roleName FROM Role r WHERE r.roleName = :roleName")
//	Role findByRoleName(UserRoles roleName);
	
	//@Query("SELECT r.roleName FROM Role r WHERE r.roleName = :roleName")
	Optional<Role> findByRoleName(UserRoles roleName);

}
