package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogInController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView showLogin() {
        ModelAndView modelView = new ModelAndView("fragments/login");
        modelView.addObject("user", new User());
        return modelView;
    }

    @RequestMapping("/logout")
    public String logOut() {
        return "";
    }

    @RequestMapping(value = "/proccesLogin", method = RequestMethod.POST)
    public ModelAndView loginProcess(HttpServletRequest request, HttpServletResponse response,
                                     @ModelAttribute("login") User user) {
        ModelAndView mav = null;
      /*  User user = userService.validateUser(login);
        if (null != user) {
            mav = new ModelAndView("welcome");
            mav.addObject("firstname", user.getFirstname());
        } else {
            mav = new ModelAndView("login");
            mav.addObject("message", "Username or Password is wrong!!");
        }
        */
        return mav;
    }

}
