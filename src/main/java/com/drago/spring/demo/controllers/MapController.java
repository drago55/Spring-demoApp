package com.drago.spring.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

	@GetMapping("/map")
	public String index() {
		return "map/index";
	}

	@GetMapping("/fullscreen_map")
	public String fullScreenMap() {
		return "map/fullscreen_map";

	}
}
