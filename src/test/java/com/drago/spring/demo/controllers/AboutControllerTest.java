package com.drago.spring.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AboutControllerTest {
	
	private MockMvc mockMvc;
	
	private AboutController aboutController;
	
	@Before
	public void setup() {
		aboutController = new AboutController();
		mockMvc = MockMvcBuilders.standaloneSetup(aboutController).build();
	}

    @Test
    public void showContact() throws Exception {
    	mockMvc.perform(get("/about")).andExpect(status().isOk())
    	.andExpect(view().name("about/index"));
    }

}
