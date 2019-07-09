package com.drago.spring.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

	@GetMapping("about")
	public String showAbout() {
		return "about/index";
	}

}
