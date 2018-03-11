package com.drago.spring.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {


    @RequestMapping({"","/","index"})
    public String index(Model model){
        model.addAttribute("content","google_map");
        return "index";
    }
    @RequestMapping("fullscreen_map")
    public String fullScreenMap(Model model){
        model.addAttribute("content","fullscreen_map");
        return "fragments/fullscreen_map";
    }
}
