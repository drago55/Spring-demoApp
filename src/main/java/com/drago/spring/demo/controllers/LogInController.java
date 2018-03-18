package com.drago.spring.demo.controllers;

import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.domain.UserLoginDto;
import com.drago.spring.demo.exception.InvalidUserException;
import com.drago.spring.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@Slf4j
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
    public ModelAndView processLogin(@ModelAttribute("userLoginDto") @Valid UserLoginDto userLoginDto,
                                     BindingResult result, Model model) {
        ModelAndView mav = new ModelAndView("fragments/login");
        try {

            if (userService.isValidUser(userLoginDto)) {
                log.debug("user is VALID");
                UserDetails user = userService.loadUserByUsername(userLoginDto.getUserEmail());
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                mav = new ModelAndView("index");
                mav.addObject("content","google_map");
                mav.addObject("firstname", user.getUsername());

                log.debug(user.toString());

                return mav;
            }
        } catch (InvalidUserException e) {
            mav = new ModelAndView("fragments/login");
            mav.addObject("error", e.getMessage());
            mav.addObject(userLoginDto);
            log.debug("exception "+e.getMessage());
            return mav;
        }
        log.debug("returning default view");
        return mav;
    }


}
