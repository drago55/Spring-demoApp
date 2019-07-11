package com.drago.spring.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.drago.spring.demo.data_transfer_objects.UserLoginDto;
import com.drago.spring.demo.exception.InvalidUserException;
import com.drago.spring.demo.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LogInController {

	private static final String LOGIN_INDEX = "login/index";
	
	@Autowired
	private UserService userService;

	@GetMapping(value = "/login")
	public String showLogin(Model model) {
		log.debug("Opening login page.");
		model.addAttribute("userLoginDto", new UserLoginDto());
		return LOGIN_INDEX;
	}

	@GetMapping(value = "logout")
	public String logOut() {
		log.debug("Logout redirect to home page.");
		return "redirect:/";
	}

	@PostMapping(value = "processLogin")
	public String processLogin(@ModelAttribute("userLoginDto") @Valid UserLoginDto userLoginDto, BindingResult result,
			Model model) {

		log.debug("Processing user login.");
		try {

			if (userService.isValidUser(userLoginDto)) {

				UserDetails user = userService.loadUserByUsername(userLoginDto.getUserEmail());
				Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				model.addAttribute("user", user);
				return "redirect:/";
			}
		} catch (InvalidUserException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("userLoginDto", userLoginDto);
			return LOGIN_INDEX;
		}

		return LOGIN_INDEX;
	}

}
