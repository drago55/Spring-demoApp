package com.drago.spring.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ContactControllerTest {
	
	private MockMvc mockMvc;
	
	private ContactController contactController;
	
	@Before
	public void setup() {
		contactController = new ContactController();
		mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
	}

    @Test
    public void showContact() throws Exception {
    	mockMvc.perform(get("/contact")).andExpect(status().isOk())
    	.andExpect(view().name("contact/index"));
    }
  
}