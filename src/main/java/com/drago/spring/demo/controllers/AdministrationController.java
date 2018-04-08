package com.drago.spring.demo.controllers;

import com.drago.spring.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdministrationController {

    @Autowired
    private UserService userService;


    @RequestMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/showUsers";
    }

    @RequestMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {

        return "";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        return "";
    }
}
