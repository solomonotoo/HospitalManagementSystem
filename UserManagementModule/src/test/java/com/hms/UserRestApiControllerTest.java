package com.hms;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.controller.UserRestApiController;
import com.hms.modules.Role;
import com.hms.modules.User;
import com.hms.modules.UserRoles;
import com.hms.repository.RoleRepository;
import com.hms.services.impl.UserServiceImpl;

@AutoConfigureMockMvc
@WebMvcTest(UserRestApiController.class)
@Rollback(false)
public class UserRestApiControllerTest {

	//Endpoint url
	private static final String END_POINT_PATH = "/api/v1/users";
	
	@MockitoBean
	private UserServiceImpl userService;
	
	@Autowired MockMvc mockMvc;
	
	
	@Autowired private ObjectMapper mapper; //for converting java object to json object
	
	//controller test return empty list of users
	@Test
	public void testListUserShouldReturn204NoContent() throws Exception {
		when(userService.listUsers()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(END_POINT_PATH))
			.andExpect(status().isNoContent())
			.andDo(print());
	}
	
	//controller test that retrieve list users
//	@Test
//	public void testListUsersShouldReturn200OK() throws Exception {
//		Role role = new Role();
//        role.setRoleId(1);
//        role.setRoleName(UserRoles.ROLE_ADMIN);
//        
//		//create use object
//		User user = new User()
//					.firstName("Peter")
//					.maidenName("Osei")
//					.lastName("Owusu")
//					.email("osei1@gmail.com")
//					.dateOfBirth(LocalDate.now())
//					.userName("oseiwusu")
//					.password("12345678")
//					.phoneNumber("123456789");
//			
//			
//			//fakes the listUses method in the UserServiceImpl class
//			when(userService.listUsers()).thenReturn(List.of(user));
//			
//			mockMvc.perform(get(END_POINT_PATH))
//					.andExpect(status().isOk())
//					.andDo(print());
//	}
	
	
	//controller test method that returns bad request for invalid data or when 
	// a required field is empty or null
//	@Test
//	public void testShouldReturn400BadRequest() throws Exception {
//		Role role = new Role();
//        role.setRoleId(1);
//        role.setRoleName(UserRoles.ROLE_ADMIN);
//        
//		//create use object
//		User user = new User()
//					.firstName("Peter")
//					.maidenName("O")
//					.lastName("Owusu")
//					.email("osei@gmail.com")
//					.dateOfBirth(LocalDate.now())
//					.userName("")
//					.password("12345678")
//					.phoneNumber("123456789");
//			user.setRolesFromJson(role);
//			
//			//fakes the UserServiceImpl class
//			//Mockito.when(userService.createUser(user)).thenReturn(user);
//			
//			
//			//convert user object to json object using object mapper
//			String bodyContent = mapper.writeValueAsString(user);
//			
//			System.out.println("Request Content: " + bodyContent);
//			
//			
//			
//			mockMvc.perform(post(END_POINT_PATH)
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(bodyContent))
//				.andExpect(status().isBadRequest())
//				.andDo(print());
//			
//	}
//	
	
	@Test
	public void testAddUserShouldReturn201Created() throws Exception {
		Role role = new Role();
        role.setRoleId(1);
        role.setRoleName(UserRoles.ROLE_ADMIN);
	
		
		User user = new User()
				.email("admin@gmail.com")
				.userName("admin")
				.password("12345")
				.accountNonLocked(true)
				.accountEnabled(true)
				.accountExpiryDate(LocalDate.now())
				.accountNonExpired(true)
				.credentialsNonExpired(true)
				.credentialsExpiryDate(LocalDate.now())
				.isTwoFactorEnabled(false)
				.signUpMethod("email");
			user.setRolesFromJson(role);

		//fakes the UserServiceImpl class
		Mockito.when(userService.createUser(user)).thenReturn(user);
		
		
		//convert user object to json object using object mapper
		String bodyContent = mapper.writeValueAsString(user);
		
		System.out.println("Request Content: " + bodyContent);
		
		
		
		mockMvc.perform(post(END_POINT_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(bodyContent))
			.andExpect(status().isCreated())
			//without hashcode and equals method in User.java you will get issues with .andExpect(content().contentType(MediaType.APPLICATION_JSON)) .andExpect(jsonPath("$.first_name", is("Peter")))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.username", is("admin")))
			.andDo(print());
		
	}
	
	//test that returns bad request for invalid data for update user
	
//	@Test
//	public void testUpdateShouldReturn400BadRequest() throws Exception {
//		User user = new User()
//				.id(1L)
//				.firstName("Peter")
//				.maidenName("Osei")
//				.lastName("")
//				.dateOfBirth(LocalDate.now())
//				.phoneNumber("123456789");
//		
//		String requestBody = mapper.writeValueAsString(user);
//		
//		mockMvc.perform(put(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON).content(requestBody))
//		.andExpect(status().isBadRequest())
//		.andDo(print());
//		
//	}
	
	
}
