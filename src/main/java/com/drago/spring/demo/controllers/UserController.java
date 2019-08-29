package com.drago.spring.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.drago.spring.demo.data_transfer_objects.MapOptionsDto;
import com.drago.spring.demo.data_transfer_objects.UserProfileDto;
import com.drago.spring.demo.services.UserProfileService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserProfileService userProfileService;

	@GetMapping("/profile")
	public String showUserProfile(Model model) {
		log.debug("Showing user profile page.");
		
		UserProfileDto userProfile = userProfileService.getUserProfile();
			
		model.addAttribute("userProfile", userProfile);
		
		return "user/profile";
	}
	
	@GetMapping("/profile/mapOptions")
	public @ResponseBody MapOptionsDto getMapOptions() {
		log.debug("Getting user map options...");
		return userProfileService.getUserProfile().getMapOptions();
	}
	
	@PostMapping("/profile/mapOptions")
	public String saveUserMapOptions(@ModelAttribute("userMapOptions") @Valid UserProfileDto userProfileDto, BindingResult result,
			Model model) {
		log.debug("Saving user profile...");
		
		if (!result.hasErrors()) {
			UserProfileDto userProfile = userProfileService.save(userProfileDto);
			model.addAttribute("userProfile", userProfile);
			model.addAttribute("message", "Map options saved!");
			return "user/profile";
		}
		model.addAttribute("error", "Error saving map options...");
		return "user/profile";
	}

}
