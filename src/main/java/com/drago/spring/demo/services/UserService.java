package com.drago.spring.demo.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.drago.spring.demo.data_transfer_objects.UserDto;
import com.drago.spring.demo.data_transfer_objects.UserLoginDto;
import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.exception.InvalidUserException;

public interface UserService extends UserDetailsService {

	User findUserByEmail(String email);

	User save(UserRegistrationDto userRegistrationDto);

	boolean isValidUser(UserLoginDto userLoginDto) throws InvalidUserException;

	List<UserDto> findAll();

	User findUseById(Long id);

	User getAuthenticatedUser();

}
