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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

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

    @RequestMapping("/showUsers")
    public String showUsers(Model model) {
        log.debug("/showUsers firing");

        //TODO implement list of all users

        return "index";
    }

    @RequestMapping("/showMarkers")
    public String showMarkers(Model model) {

        List<Marker> markers= markerRepositry.findAll();
        model.addAttribute("markers",markers);
        model.addAttribute("content", "showMarkers");

        return "index";
    }

    @RequestMapping("/deleteMarker/{id}")
    public String showDelete(@PathVariable Long id, Model model){
        model.addAttribute("content","deleteMarker");
        model.addAttribute("id",id);
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
            model.addAttribute("success" , isSuccessful);
            model.addAttribute("marker",marker);
        }


        return "redirect:/createMarker";
    }

    @RequestMapping(value = "/updateMarker", method = RequestMethod.POST)
    public String updateMarker(@ModelAttribute("marker") @Valid Marker marker,
                               BindingResult result, Model model) {
        log.debug("update firing");
        model.addAttribute("content", "createMarker");

        return "index";
    }

    @RequestMapping("deleteMarker/delete/{id}")
    public String deleteMarker(@PathVariable Long id, Model model) {

        try {

            markerService.deleteMarkerById(id);

        }catch (RuntimeException e){

            model.addAttribute("error"+e.getMessage());
            model.addAttribute("id",id);
            model.addAttribute("content","deleteMarker");
            return "index";
        }


        return "redirect:/showMarkers";
    }
}


