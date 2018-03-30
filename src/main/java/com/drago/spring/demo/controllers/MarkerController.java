package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.Image;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.MarkerType;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.services.ImageService;
import com.drago.spring.demo.services.MarkerService;
import com.drago.spring.demo.services.StorageService;
import com.drago.spring.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Slf4j
public class MarkerController {

    @Autowired
    private UserService userService;

    @Autowired
    private MarkerService markerService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private StorageService storageService;

    private boolean isSuccessful = false;

    @RequestMapping(value = {"markers/get_json", "index/markers/get_json", "fullscreen_map/markers/get_json"})
    public @ResponseBody
    List<Marker> produceJson() {
        return markerService.findAllMarkers();
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

    @RequestMapping("marker/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        model.addAttribute("marker", markerService.findMarkerById(id));
        return "marker/deleteMarker";
    }

    @RequestMapping("deleteAction/{id}")
    public String deleteAction(@PathVariable Long id, Model model) {
        markerService.deleteMarkerById(id);
        return "redirect:/markers";
    }


    @RequestMapping("marker/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {


        model.addAttribute("marker", markerService.findMarkerById(id));

        model.addAttribute("enum", MarkerType.values());

        return "marker/showUpdateMarker";

    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("marker") @Valid Marker marker, @RequestParam("file") MultipartFile[] files, BindingResult result, Model model) {

        log.debug("Marker type " + marker.getMarkerType());

        if (!result.hasErrors()) {

            storageService.setUserDir(Paths.get(userService.getAuthenticatedUser().getLastName()));

            User user = userService.getAuthenticatedUser();

            marker.setUser(user);

            List<MultipartFile> listOfFiles = getCollection(files);

            uploadFiles(listOfFiles);

            setImagePaths(marker, listOfFiles);

            markerService.save(marker);
            isSuccessful = true;
            model.addAttribute("success", isSuccessful);
            model.addAttribute("marker", marker);
        }
        return "redirect:/markers";
    }

    private void setImagePaths(@ModelAttribute("marker") @Valid Marker marker, List<MultipartFile> listOfFiles) {
        listOfFiles.iterator().forEachRemaining(multipartFile -> marker.addImage(new Image(multipartFile.getOriginalFilename(), multipartFile.getOriginalFilename())));
    }

    private List<MultipartFile> getCollection(@RequestParam("file") MultipartFile[] files) {
        return Stream.of(files).filter((multipartFile) -> !multipartFile.isEmpty()).collect(Collectors.toList());
    }

    private void uploadFiles(List<MultipartFile> files) {
        files.forEach((multipartFile -> storageService.store(multipartFile)));
    }


    @RequestMapping(value = "/updateMarker", method = RequestMethod.POST)
    public String updateMarker(@ModelAttribute("marker") @Valid Marker marker, BindingResult result, Model model) {
        log.debug("execute update to database");

        try {
            markerService.save(marker);

            model.addAttribute("marker", marker);

            List<String> str;
            List<byte[]> list = new ArrayList<>();
            marker.getImages().forEach(image -> {

                list.add(Base64.getDecoder().decode(image.getImage()));
            });
            model.addAttribute("imge", list);
            model.addAttribute("markerType", MarkerType.values());

            return "redirect:/marker/edit/" + marker.getId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/markers";
    }


}


