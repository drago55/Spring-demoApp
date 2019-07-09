package com.drago.spring.demo.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.drago.spring.demo.data_transfer_objects.MarkerDto;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.MarkerType;
import com.drago.spring.demo.services.ImageService;
import com.drago.spring.demo.services.MarkerDtoService;
import com.drago.spring.demo.services.MarkerService;
import com.drago.spring.demo.services.StorageService;
import com.drago.spring.demo.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MarkerController {

	@Autowired
	private UserService userService;

	@Autowired
	private MarkerService markerService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private MarkerDtoService markerDtoService;

	private boolean isSuccessful = false;

	@RequestMapping(value = { "markers/get_json", "index/markers/get_json", "fullscreen_map/markers/get_json" })
	public @ResponseBody Set<MarkerDto> produceJson() {
		return markerDtoService.getAllMarkers();
	}

	@RequestMapping("/markers")
	public String showMarkers(Model model) {
		model.addAttribute("markers", markerService.findAllMarkers());
		return "marker/index";
	}

	@RequestMapping("/marker")
	public String createMarker(Model model) {

		model.addAttribute("marker", new Marker());
		model.addAttribute("enum", MarkerType.values());
		return "marker/markerForm";
	}

	@RequestMapping("/marker/delete/{id}")
	public String delete(@PathVariable Long id, Model model) {

		model.addAttribute("marker", markerService.findMarkerById(id));
		return "marker/deleteMarker";
	}

	@RequestMapping("/deleteAction/{id}")
	public String deleteAction(@PathVariable Long id, Model model) {
		markerService.findMarkerById(id).getImages().stream().forEach(image -> imageService.deleteImage(image));
		markerService.deleteMarkerById(id);
		return "redirect:/markers";
	}

	@RequestMapping("/marker/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {

		model.addAttribute("marker", markerService.findMarkerById(id));

		model.addAttribute("enum", MarkerType.values());

		return "marker/markerForm";

	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("marker") @Valid Marker marker, @RequestParam("file") MultipartFile[] files,
			BindingResult result, Model model) {

		if (!result.hasErrors()) {

			markerService.save(marker, files);
			isSuccessful = true;
			model.addAttribute("success", isSuccessful);
			model.addAttribute("marker", marker);
		}
		return "redirect:/markers";
	}

	private void addImages(Marker marker, List<MultipartFile> listOfFiles) {
		List<String> listOfFileNames = storageService.getFileNames();
		Map<String, MultipartFile> map = new HashMap<>();
		// listOfFiles.forEach(m);
		listOfFiles.iterator().forEachRemaining(multipartFile -> {

			// marker.addImage(new Image(multipartFile.getOriginalFilename(),
			// storageService.getFileNames()));
			;

		});
	}

	private String getImagePath(MultipartFile multipartFile) throws IOException {
		Path path = Paths.get(storageService.loadAsResource(multipartFile.getOriginalFilename()).getURI());
		final int len = path.getNameCount();
		return path.subpath(0, len).toString();
	}

	private List<MultipartFile> filterAndGetCollection(MultipartFile[] files) {
		return Stream.of(files).filter((multipartFile) -> !multipartFile.isEmpty()).collect(Collectors.toList());
	}

	private void uploadFiles(List<MultipartFile> files) {
		files.forEach((multipartFile -> {
			storageService.store(multipartFile);
		}));
	}

}
