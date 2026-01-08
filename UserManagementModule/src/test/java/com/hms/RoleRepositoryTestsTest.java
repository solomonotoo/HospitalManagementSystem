package com.hms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.hms.modules.Role;
import com.hms.modules.UserRoles;
import com.hms.repository.RoleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTestsTest {
	
	@Autowired
	private RoleRepository roleRepo;
	
	//test method that create a single role
//	@Test
//	public void testCreateRole() {
//		
//		Role admin = new Role();
//		admin.setRoleName(UserRoles.ROLE_ADMIN);
//		admin.getRoleDescription();
//		Role savedRole = roleRepo.save(admin);
//		
//		assertThat(savedRole.getRoleName()).isNotNull();
//	}
	
	
	//repository test method that create a list of roles
	@Test
	public void testCreateRoles() {
		Role user = new Role();
		user.setRoleName(UserRoles.ROLE_USER);
		Role nurse = new Role();
		nurse.setRoleName(UserRoles.ROLE_NURSE);
		
		Role doctor = new Role();
		 doctor.setRoleName(UserRoles.ROLE_DOCTOR);
		 Role phamacist = new Role();
		 phamacist.setRoleName(UserRoles.ROLE_PHARMACIST);
		Role staff = new Role();
		staff.setRoleName(UserRoles.ROLE_BILLING_STAFF);
		
		Role receptionist = new Role();
		receptionist.setRoleName(UserRoles.ROLE_RECEPTIONIST);
		
		roleRepo.saveAll(List.of(user,nurse, doctor,phamacist, staff, receptionist));
	}

}
