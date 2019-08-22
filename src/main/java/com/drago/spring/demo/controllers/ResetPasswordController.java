package com.drago.spring.demo.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.drago.spring.demo.services.SecurityService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/registration")
@Slf4j
public class ResetPasswordController {
	
	@Autowired
	private SecurityService securityService;

	@GetMapping("/forgotPasswordPage")
	public String showForgotPasswordPage(Model model) {
		return "registration/forgotPassword";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail, RedirectAttributes redirectAttrs) {
		 
		securityService.sendResetPassword(getAppUrl(request), userEmail);
		
		redirectAttrs.addFlashAttribute("message", "We will send you a link to reset password.");
		
		return "redirect:/login";
	}

	@GetMapping(value = "/changePassword/{id}/{token}")
	public String showChangePasswordPage(Model model, @PathVariable("id") Long id, @PathVariable("token") String token) {

		String result = securityService.validatePasswordResetToken(id, token);
		if (result != null) {
			model.addAttribute("error", "Greska" + result);
			return "redirect:/login";
		}
		return "redirect:/registration/showUpdatePassword";
	}
	
	@GetMapping(value = "/showUpdatePassword")
	public String showUpdatePassword(Model model) {
		
		log.info("Showing password update page.");
		
		model.addAttribute("password", new PasswordDto());

		return "/registration/updatePassword";
	}
	
	@PostMapping(value = "/savePassword")
	public String saveNewPassword(@ModelAttribute("password") @Valid PasswordDto passwordDto, BindingResult result,  RedirectAttributes redirectAttrs,
			Model model) {
		
		
		
		redirectAttrs.addFlashAttribute("password", passwordDto);
		redirectAttrs.addFlashAttribute("message", "");
		
		if (!result.hasErrors()) {
			log.info("Saving new password");
			redirectAttrs.addFlashAttribute("message", "Password changed succesfully!");
			return "redirect:/login";
		}
		log.info("There are errors...");
		
		return "registration/updatePassword";
	}
	
	 private String getAppUrl(HttpServletRequest request) {
	        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	 }

}
