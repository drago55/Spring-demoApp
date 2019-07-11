package com.drago.spring.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
import com.drago.spring.demo.exception.EmailExistsException;
import com.drago.spring.demo.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RegisterUserController {

	private static final String REGISTRATION_INDEX = "/registration/index";

	@Autowired
	private UserService userService;

	@GetMapping(value = "/registerUser")
	public String showRegisterUser(Model model) {
		log.debug("Showing user registration page.");
		model.addAttribute("user", new UserRegistrationDto());
		return REGISTRATION_INDEX;
	}

	@PostMapping(value = "/processRegistration")
	public String proccesRegistration(@ModelAttribute("user") @Valid UserRegistrationDto userRegistrationDto,
			BindingResult result, Model model) {
		log.debug("Starting process of user registration.");
		boolean isSuccessful = false;
		model.addAttribute("user", userRegistrationDto);
		
		if (!result.hasErrors()) {
			
			userService.save(userRegistrationDto);
			isSuccessful = true;
			model.addAttribute("success", isSuccessful);
			log.debug("User registration completed sucessfull.");
			return REGISTRATION_INDEX;

		}
		isSuccessful = false;
		log.debug("User registration failed.");
		model.addAttribute("success", isSuccessful);
		return REGISTRATION_INDEX;
	}

}
