package com.drago.spring.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class MapControllerTest {
	
	private MockMvc mockMvc;
	
	private MapController mapController;
	
	@Before
	public void setup() {
		mapController = new MapController();
		mockMvc = MockMvcBuilders.standaloneSetup(mapController).build();
	}

    @Test
    public void showMap() throws Exception {
    	mockMvc.perform(get("/index")).andExpect(status().isOk())
    	.andExpect(view().name("map/index"));
    }
    
    @Test
    public void showMapOnRoot() throws Exception {
    	mockMvc.perform(get("/")).andExpect(status().isOk())
    	.andExpect(view().name("map/index"));
    }
    
    @Test
    public void showMapOnEmptyUrl() throws Exception {
    	mockMvc.perform(get("")).andExpect(status().isOk())
    	.andExpect(view().name("map/index"));
    }
    
    @Test
    public void showFullscreenMap() throws Exception {
    	mockMvc.perform(get("/fullscreen_map")).andExpect(status().isOk())
    	.andExpect(view().name("map/fullscreen_map"));
    }
}