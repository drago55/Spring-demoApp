package com.drago.spring.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drago.spring.demo.data_transfer_objects.UserProfileDto;
import com.drago.spring.demo.services.UserProfileService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserProfileService userProfileService;

	@GetMapping(value = "/profile")
	public String showRegisterUser(Model model) {
		log.debug("Showing user profile page.");
		
		UserProfileDto userProfile = userProfileService.getUserProfile();
			
		model.addAttribute("userProfile", userProfile);
		return "user/profile";
	}

}
