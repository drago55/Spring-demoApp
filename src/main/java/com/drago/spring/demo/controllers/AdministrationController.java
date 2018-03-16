package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.LatLon;
import com.drago.spring.demo.domain.Location;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.MarkerType;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdministrationController {

    //private MarkerRepositry markerRepositry;

    @RequestMapping("/addUser")
    public String addUser() {
        return "";
    }

    @RequestMapping(value = "/showMarkers", method = RequestMethod.GET)
    public String showMarkers(Model model) {

        model.addAttribute("content", "showMarkers");

        return "index";
    }

    @RequestMapping("/editMarker")
    public String editMarker(Model model) {
        model.addAttribute("content", "createMarker");

        return "index";
    }

    @RequestMapping("/deleteMarker")
    public String deleteMarker(Model model) {
        model.addAttribute("content", "showMarkers");

        return "index";
    }

    @RequestMapping("/addMarker")
    public String addMarker(Model model) {
        // TODO implement add marker

        return "";
    }

    @RequestMapping(value = "/showCreateMarker", method = RequestMethod.GET)
    public String createMarker(Model model) {

        model.addAttribute("marker", new Marker());
        model.addAttribute("location", new Location());
        model.addAttribute("latLon", new LatLon());
        model.addAttribute("markerType", MarkerType.values());

        return "fragments/createMarker";
    }
}
