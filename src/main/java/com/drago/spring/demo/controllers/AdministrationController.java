package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.*;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import com.drago.spring.demo.repositories.MarkerRepository;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class AdministrationController {

    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MarkerService markerService;

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
        log.debug("update firing");
        Marker marker= null;
        try {
            marker = markerService.findMarkerById(id);
        } catch (NoSuchMarkerException e) {
            e.printStackTrace();
        }
        model.addAttribute("marker", marker);
        model.addAttribute("markerType", MarkerType.values());

        return "admin/marker";

    }


    @RequestMapping(value = "/insertMarker", method = RequestMethod.POST)
    public String addMarker(@ModelAttribute("marker") @Valid Marker marker, BindingResult result, Model model) {

        User user = getAuthenticatedUser();

        marker.setUser(user);

        log.debug("" + marker);

        if (!result.hasErrors()) {

            markerService.save(marker);
            isSuccessful = true;
            model.addAttribute("success", isSuccessful);
            model.addAttribute("marker", marker);
        }
        return "admin/marker";
    }


    @RequestMapping(value = "/updateMarker", method = RequestMethod.POST)
    public String updateMarker(@ModelAttribute("marker") @Valid Marker marker, BindingResult result, Model model) {
        log.debug("update firing");

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


