package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.Image;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.MarkerType;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.services.ImageService;
import com.drago.spring.demo.services.MarkerService;
import com.drago.spring.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Controller
@Slf4j
public class MarkerController {

    @Autowired
    private UserService userService;

    @Autowired
    private MarkerService markerService;

    @Autowired
    private ImageService imageService;

    private boolean isSuccessful = false;


    @RequestMapping("/markers")
    public String showMarkers(Model model) {
        model.addAttribute("markers", markerService.findAllMarkers());
        return "marker/index";
    }

    @RequestMapping("/marker")
    public String createMarker(Model model) {

        model.addAttribute("marker", new Marker());
        model.addAttribute("markerType", MarkerType.values());
        return "marker/marker";
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
        model.addAttribute("markerType", MarkerType.values());

        return "marker/showUpdateMarker";

    }


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(@ModelAttribute("marker") @Valid Marker marker, @RequestParam("file") MultipartFile[] files, BindingResult result, Model model) {

        marker = setMarker(marker, files);


        if (!result.hasErrors()) {

            markerService.save(marker);
            isSuccessful = true;
            model.addAttribute("success", isSuccessful);
            model.addAttribute("marker", marker);
        }
        return "redirect:/markers";
    }

    private Marker setMarker(Marker marker, MultipartFile[] files) {

        User user = userService.getAuthenticatedUser();
        marker.setUser(user);
        //  log.debug(files[0].getOriginalFilename());

        //   log.debug("" + marker);

        Arrays.asList(files).stream().filter(multipartFile -> !multipartFile.isEmpty()).forEach((MultipartFile multipartFile) -> {

            try {

                marker.addImage(new Image(multipartFile.getOriginalFilename(),
                        Base64.getEncoder().encodeToString(multipartFile.getBytes())));

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        //    log.debug("" + marker);

        return marker;
    }


    @RequestMapping(value = "/updateMarker", method = RequestMethod.POST)
    public String updateMarker(@ModelAttribute("marker") @Valid Marker marker, BindingResult result, Model model) {
        log.debug("execute update to database");

        try {
            markerService.save(marker);

            model.addAttribute("marker", marker);
            model.addAttribute("markerType", MarkerType.values());

            return "fragments/createMarker";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/markers";
    }


}


