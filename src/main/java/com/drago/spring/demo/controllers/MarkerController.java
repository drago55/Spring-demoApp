package com.drago.spring.demo.controllers;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.drago.spring.demo.data_transfer_objects.MarkerDto;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.MarkerType;
import com.drago.spring.demo.services.ImageService;
import com.drago.spring.demo.services.MarkerDtoService;
import com.drago.spring.demo.services.MarkerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MarkerController {

	private static final String MARKER_TYPE = "enum";

	private static final String MARKER = "marker";

	@Autowired
	private MarkerService markerService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private MarkerDtoService markerDtoService;

	@GetMapping(value = { "markers/get_json", "index/markers/get_json", "fullscreen_map/markers/get_json" })
	public @ResponseBody Set<MarkerDto> produceJson() {
		log.debug("Rest api has been called! Serving json content!");
		return markerDtoService.getAllMarkers();
	}

	@GetMapping("/markers")
	public String showMarkers(Model model) {
		log.debug("Show markers.");
		model.addAttribute("markers", markerService.findAllMarkers());
		return "marker/index";
	}

	@GetMapping("/marker")
	public String createMarker(Model model) {
		log.debug("Marker form.");
		model.addAttribute(MARKER, new Marker());
		model.addAttribute(MARKER_TYPE, MarkerType.values());
		return "marker/markerForm";
	}

	@GetMapping("/marker/delete/{id}")
	public String delete(@PathVariable Long id, Model model) {
		log.debug("Delete marker? With id: " + id);
		model.addAttribute(MARKER, markerService.findMarkerById(id));
		return "marker/deleteMarker";
	}

	@GetMapping("/deleteAction/{id}")
	public String deleteAction(@PathVariable Long id, Model model) {
		log.debug("Deleting marker with id: " + id);
		markerService.findMarkerById(id).getImages().stream().forEach(image -> imageService.deleteImage(image));
		markerService.deleteMarkerById(id);
		return "redirect:/markers";
	}

	@GetMapping("/marker/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		log.debug("Editing marker with id: " + id);
		model.addAttribute(MARKER, markerService.findMarkerById(id));
		model.addAttribute(MARKER_TYPE, MarkerType.values());
		return "marker/markerForm";

	}

	@PostMapping(value = "/save")
	public String save(@ModelAttribute(MARKER) @Valid Marker marker, @RequestParam("file") MultipartFile[] files,
			BindingResult result, Model model) {
		log.debug("Saving/Update marker...");
		boolean isSuccessful = false;

		if (!result.hasErrors()) {

			markerService.save(marker, files);
			isSuccessful = true;
			model.addAttribute("success", isSuccessful);
			model.addAttribute(MARKER, marker);
		}
		return "redirect:/markers";
	}

}
