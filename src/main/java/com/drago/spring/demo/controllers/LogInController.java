package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.UserLoginDto;
import com.drago.spring.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LogInController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String showLogin(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "fragments/login";
    }

    @RequestMapping("logout")
    public String logOut() {
        return "";
    }

    @RequestMapping(value = "processLogin", method = RequestMethod.POST)
    public String processLogin(@ModelAttribute("userLoginDto") @Valid UserLoginDto userLoginDto,
                                     BindingResult result, Model model) {

         model.addAttribute("userLoginDto", userLoginDto);

/*
        if (userService.isValidUser(userLoginDto)) {
            User user = userService.findUserByEmail(userLoginDto.getUserEmail());
            mav = new ModelAndView("welcome");
            mav.addObject("firstname", user.getFirstname());
        } else {
            mav = new ModelAndView("login");
            mav.addObject("message", "Username or Password is wrong!!");
        }
*/
        return "fragments/login";
    }

}
