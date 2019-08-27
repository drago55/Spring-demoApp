package com.drago.spring.demo.controllers;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.drago.spring.demo.data_transfer_objects.PasswordDto;
import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.services.SecurityService;
import com.drago.spring.demo.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {

	private static final String REDIRECT_LOGIN = "redirect:/login";

	private static final String REGISTRATION_INDEX = "/registration/index";

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserService userService;

	@GetMapping(value = "/user")
	public String showRegisterUser(Model model) {
		log.debug("Showing user registration page.");
		model.addAttribute("user", new UserRegistrationDto());
		return REGISTRATION_INDEX;
	}

	@PostMapping(value = "/process")
	public String proccesRegistration(HttpServletRequest request, @ModelAttribute("user") @Valid UserRegistrationDto userRegistrationDto, BindingResult result, Model model) {
		log.debug("Starting process of user registration.");
		model.addAttribute("user", userRegistrationDto);

		if (!result.hasErrors()) {

			userService.save(userRegistrationDto);
			securityService.sendVerificationTokenMail(getAppUrl(request), userRegistrationDto.getEmail());
		
			model.addAttribute("message", "You've successfully registered to our awesome app!");
			log.debug("User registration completed sucessfull.");
			return REGISTRATION_INDEX;

		}
		
		log.debug("User registration failed.");
		model.addAttribute("error", "User registration failed.");
		return REGISTRATION_INDEX;
	}

	@GetMapping("/confirm/{id}/{token}")
	public String confirmRegistration( Model model, @PathVariable("id") Long id, @PathVariable("token") String token, 
			RedirectAttributes redirectAttrs) {

		String result = securityService.validateVerificationToken(id, token);
		if (result != null) {
			redirectAttrs.addFlashAttribute("error", "Error " + result);
			return REDIRECT_LOGIN;
		}
		
		userService.enableUser(id);

		securityService.deleteToken(token);
		
		redirectAttrs.addFlashAttribute("message", "You have successfully confirmed your account. Please login...");
		
		return REDIRECT_LOGIN;
	}

	@GetMapping("/forgotPasswordPage")
	public String showForgotPasswordPage(Model model) {
		return "registration/forgotPassword";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail, RedirectAttributes redirectAttrs) {

		securityService.sendResetPasswordMail(getAppUrl(request), userEmail);

		redirectAttrs.addFlashAttribute("message", "We will send you a link to reset password.");

		return REDIRECT_LOGIN;
	}

	@GetMapping(value = "/changePassword/{id}/{token}")
	public String showChangePasswordPage(Model model, @PathVariable("id") Long id, @PathVariable("token") String token, RedirectAttributes redirectAttrs) {

		String result = securityService.validateVerificationToken(id, token);
		if (result != null) {
			redirectAttrs.addFlashAttribute("error", "Error " + result);
			return REDIRECT_LOGIN;
		}
		securityService.deleteToken(token);
		securityService.grantUserAutority(id,  Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		return "redirect:/registration/showUpdatePassword";
	}

	@GetMapping(value = "/showUpdatePassword")
	public String showUpdatePassword(Model model) {

		log.info("Showing password update page.");

		model.addAttribute("password", new PasswordDto());

		return "/registration/updatePassword";
	}

	@PostMapping(value = "/savePassword")
	public String saveNewPassword(@ModelAttribute("password") @Valid PasswordDto passwordDto, BindingResult result, RedirectAttributes redirectAttrs, Model model, HttpServletRequest request) {

		redirectAttrs.addFlashAttribute("password", passwordDto);
		redirectAttrs.addFlashAttribute("message", "");

		if (!result.hasErrors()) {
			log.info("Saving new password");

			redirectAttrs.addFlashAttribute("message", "Password changed succesfully!");

			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			userService.changeUserPassword(user, passwordDto.getPassword());
			
			userService.enableUser(user.getId());

			userService.logoutUser(request);

			return REDIRECT_LOGIN;
		}
		log.info("There are errors...");

		return "registration/updatePassword";
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}
