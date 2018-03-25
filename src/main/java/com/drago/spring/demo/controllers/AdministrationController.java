package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.*;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import com.drago.spring.demo.repositories.MarkerRepository;
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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Controller
@Slf4j
public class AdministrationController {

    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MarkerService markerService;

    @Autowired
    private ImageService imageService;

    private boolean isSuccessful = false;

    @RequestMapping("/showUsers")
    public String showUsers(Model model) {
        log.debug("/showUsers firing");
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/showUsers";
    }

    @RequestMapping("/showMarkers")
    public String showMarkers(Model model) {
        List<Marker> markers = markerRepository.findAll();
        model.addAttribute("markers", markers);
        return "admin/index";
    }

    @RequestMapping("/showMarker")
    public String createMarker(Model model) {

        model.addAttribute("marker", new Marker());
        model.addAttribute("markerType", MarkerType.values());

        return "admin/marker";
    }

    @RequestMapping("/deleteMarker/{id}")
    public String showDelete(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "admin/deleteMarker";
    }

    @RequestMapping("/updateMarker/{id}")
    public String showMarker(@PathVariable Long id, Model model) {
        log.debug("show update ");
        Marker marker = null;
        try {
            marker = markerService.findMarkerById(id);
        } catch (NoSuchMarkerException e) {
            e.printStackTrace();
        }
        model.addAttribute("marker", marker);
        model.addAttribute("markerType", MarkerType.values());

        return "admin/showUpdateMarker";

    }


    @RequestMapping(value = "/insertMarker", method = RequestMethod.POST)
    public String addMarker(@ModelAttribute("marker") @Valid Marker marker, @RequestParam("file") MultipartFile[] files, BindingResult result, Model model) {

        marker = setMarker(marker, files);


        if (!result.hasErrors()) {

            markerService.save(marker);
            isSuccessful = true;
            model.addAttribute("success", isSuccessful);
            model.addAttribute("marker", marker);
        }
        return "redirect:/showMarkers";
    }

    private Marker setMarker(Marker marker, MultipartFile[] files) {

        User user = getAuthenticatedUser();
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

        return "redirect:/showMarkers";
    }


    @RequestMapping("delete/{id}")
    public String deleteMarker(@PathVariable Long id, Model model) {

        markerService.deleteMarkerById(id);

        return "redirect:/showMarkers";
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userService.findUserByEmail(authentication.getName());
    }
}


