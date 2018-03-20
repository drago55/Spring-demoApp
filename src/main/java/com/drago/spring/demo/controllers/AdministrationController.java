package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.*;
import com.drago.spring.demo.repositories.MarkerRepositry;
import com.drago.spring.demo.services.MarkerService;
import com.drago.spring.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@Slf4j
public class AdministrationController {

    @Autowired
    private MarkerRepositry markerRepositry;

    @Autowired
    private UserService userService;

    @Autowired
    private MarkerService markerService;

    private boolean isSuccessful = false;

    @RequestMapping(value = "/showUsers", method = RequestMethod.GET)
    public String showUsers(Model model) {
        log.debug("/showUsers firing");
        //TODO implement list of all users

        return "index";
    }

    @RequestMapping(value = "/showMarkers", method = RequestMethod.GET)
    public String showMarkers(Model model) {
        log.debug("/showMarkers firing");
        //TODO implement list of all markers
        model.addAttribute("content", "showMarkers");

        return "index";
    }

    @RequestMapping(value = "/createMarker", method = RequestMethod.GET)
    public String createMarker(Model model) throws UsernameNotFoundException {


        Marker marker= new Marker();
        model.addAttribute("marker",marker);
        model.addAttribute("markerType", MarkerType.values());


        return "fragments/createMarker";
    }

    @RequestMapping(value = "/insertMarker", method = RequestMethod.POST)
    public String addMarker(@ModelAttribute("marker") @Valid Marker marker, BindingResult result, Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug(authentication.getName());

        User user=userService.findUserByEmail(authentication.getName());

        log.debug("User over email "+user);

        marker.setUser(user);

        log.debug(""+marker);

        if(!result.hasErrors()){

            markerService.save(marker);
            isSuccessful=true;
        }

        model.addAttribute("success" , isSuccessful);
        return "fragments/createMarker";
    }

    @RequestMapping(value = "/updateMarker", method = RequestMethod.POST)
    public String updateMarker(@ModelAttribute("marker") @Valid Marker marker,
                               BindingResult result, Model model) {
        log.debug("update firing");
        model.addAttribute("content", "createMarker");

        return "index";
    }

    @RequestMapping(value = "/deleteMarker", method = RequestMethod.POST)
    public String deleteMarker(@ModelAttribute("marker") @Valid Marker marker,
                               BindingResult result, Model model) {
        //TODO implement delete marker
        log.debug("delete firing");

        model.addAttribute("content", "showMarkers");

        return "index";
    }
}


