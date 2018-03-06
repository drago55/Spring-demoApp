package com.drago.spring.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SettingsController {


    @RequestMapping("admin/addUser")
    public String addUser(){
        return "";
    }

    @RequestMapping("admin/showMarkers")
    public String showMarkers(){
        return "";
    }
    @RequestMapping("admin/createMarker")
    public String createMarker(){
        return "";
    }
}
