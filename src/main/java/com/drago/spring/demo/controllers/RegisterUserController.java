package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterUserController {


    @RequestMapping(value = "/registerUser", method = RequestMethod.GET)
    public String showRegisterUser(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("content", "registerUser");
        return "index";
    }


    @RequestMapping("/processRegistration")
    public String proccesRegistration(Model model) {
        return "index";
    }


}
