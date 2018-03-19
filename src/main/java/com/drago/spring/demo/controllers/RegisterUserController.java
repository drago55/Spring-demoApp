package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.UserRegistrationDto;
import com.drago.spring.demo.exception.EmailExistsException;
import com.drago.spring.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@Slf4j
public class RegisterUserController {

    private boolean isSuccessful =false;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "registerUser", method = RequestMethod.GET)
    public String showRegisterUser(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        model.addAttribute("content", "registerUser");
        return "index";
    }


    @RequestMapping(value = "/processRegistration", method = RequestMethod.POST)
    public String proccesRegistration(@ModelAttribute("user") @Valid UserRegistrationDto userRegistrationDto,
                                             BindingResult result,  Model model) {
        model.addAttribute("user", userRegistrationDto);
        model.addAttribute("content", "registerUser");
        if (!result.hasErrors()) {
            try {
                userService.save(userRegistrationDto);

            }catch (EmailExistsException e){
                model.addAttribute("error",e.getMessage());
                 isSuccessful =false;
                return "index";
            }
            isSuccessful=true;

        }
        model.addAttribute("success" , isSuccessful);
        return "index";
    }

}
