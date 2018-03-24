package com.drago.spring.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactController {

        @RequestMapping("/contact")
        public String showContact(){
            return "contact/index";
        }
}
