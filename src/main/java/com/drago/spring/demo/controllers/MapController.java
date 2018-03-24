package com.drago.spring.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MapController {


    @RequestMapping({"","/","index"})
    public String index(){

        return "map/index";
    }
    @RequestMapping("fullscreen_map")
    public String fullScreenMap(){
         return "map/fullscreen_map";

    }
}
