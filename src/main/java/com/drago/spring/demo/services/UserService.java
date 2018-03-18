package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.domain.UserLoginDto;
import com.drago.spring.demo.domain.UserRegistrationDto;
import com.drago.spring.demo.exception.InvalidUserException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    User save(UserRegistrationDto userRegistrationDto);
    boolean isValidUser(UserLoginDto userLoginDto) throws InvalidUserException;


}
