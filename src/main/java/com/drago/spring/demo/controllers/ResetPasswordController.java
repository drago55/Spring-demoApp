package com.drago.spring.demo.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.drago.spring.demo.services.SecurityService;

@Controller
@RequestMapping("/registration")
public class ResetPasswordController {
	
	@Autowired
	private SecurityService securityService;

	@GetMapping("/forgotPasswordPage")
	public String showForgotPasswordPage(Model model) {
		return "registration/forgotPassword";
	}

	@PostMapping("/resetPassword")
	public ModelAndView resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
		
		securityService.sendResetPassword(request.getContextPath(), userEmail);
		
		return null;
	}

	@GetMapping(value = "/changePassword/{id}/{token}")
	public String showChangePasswordPage(Model model, @PathVariable("id") Long id, @PathVariable("token") String token) {

		String result = securityService.validatePasswordResetToken(id, token);
		if (result != null) {
		//	model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
			return "redirect:/login";
		}
		return "redirect:/updatePassword.html";
	}

}
