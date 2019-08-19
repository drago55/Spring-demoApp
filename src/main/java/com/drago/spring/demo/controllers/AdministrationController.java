package com.drago.spring.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.drago.spring.demo.services.UserService;

@Controller
public class AdministrationController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/users")
	public String showUsers(Model model, @PageableDefault(size = 5) Pageable pageable) {
		model.addAttribute("users", userService.findPaginatedUsers(pageable));
		return "admin/showUsers";
	}

	@GetMapping(value = "/user/disable/{id}")
	public String disableUser(@PathVariable Long id, Model model) {
		userService.disableUser(id);
		return "redirect:/users";
	}
	
	@GetMapping(value = "/user/enable/{id}")
	public String enableUser(@PathVariable Long id, Model model) {
		userService.enableUser(id);
		return "redirect:/users";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		return "";
	}
}
