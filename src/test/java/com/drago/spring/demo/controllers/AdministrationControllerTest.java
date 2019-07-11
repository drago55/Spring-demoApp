package com.drago.spring.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.drago.spring.demo.services.UserService;
import com.drago.spring.demo.services.UserServiceImpl;

public class AdministrationControllerTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService = new UserServiceImpl();

	@InjectMocks
	private AdministrationController administrationController = new AdministrationController();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this); 
		mockMvc = MockMvcBuilders.standaloneSetup(administrationController).build();
	}

	@Test
	public void testListUsers() throws Exception {
		this.mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(model().attributeExists("users"))
				.andExpect(view().name("admin/showUsers")).andDo(MockMvcResultHandlers.print());
	}

}
