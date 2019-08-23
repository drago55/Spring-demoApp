package com.drago.spring.demo.controllers;

import static org.hamcrest.CoreMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
import com.drago.spring.demo.services.UserService;
import com.drago.spring.demo.services.servicesimpl.UserServiceImpl;

public class RegisterUserControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService = new UserServiceImpl();

	@InjectMocks
	private RegistrationController registerUserController = new RegistrationController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(registerUserController).build();
	}

	@Test
	public void showRegisterUser() throws Exception {
		this.mockMvc.perform(get("/registerUser")).andExpect(status().isOk()).andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", any(UserRegistrationDto.class)))
				.andExpect(view().name("/registration/index")).andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void proccesRegistration() throws Exception {

		UserRegistrationDto user = new UserRegistrationDto();
		user.setEmail("test@net.hr");
		user.setConfirmEmail("test@net.hr");
		user.setFirstName("TestUser");
		user.setLastName("TestUser");
		user.setPassword("123456");
		user.setConfirmPassword("123456");
		/*
		 * firstName=TestUser&lastName=TestUser&email=test%40net.hr&confirmEmail=test%
		 * 40net.hr&password=123456&confirmPassword=123456
		 */
		this.mockMvc
				.perform(post("/processRegistration").param("firstName", "TestUser").param("lastName", "TestUser")
						.param("email", "test@net.hr").param("confirmEmail", "test@net.hr").param("password", "123456")
						.param("confirmPassword", "123456").contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk()).andExpect(model().attributeExists("user"))
				.andExpect(model().attributeExists("success")).andDo(MockMvcResultHandlers.print())
				.andExpect(model().attribute("user", any(UserRegistrationDto.class)))
				.andExpect(model().attribute("user", user))
				.andExpect(model().attribute("success", true));

	}
}