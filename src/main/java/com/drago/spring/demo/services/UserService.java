package com.drago.spring.demo.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
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

	User findUserById(Long id);

	User getAuthenticatedUser();

	Page<UserDto> findPaginatedUsers(Pageable pageable);
	
	void createVerificationTokenForUser(User user, String token);
	
	UserDetails loadUserByUsername(String email);
	
	void logoutUser(HttpServletRequest request);
	
	void changeUserPassword(User user, String password);

	void enableUser(Long id);
	
	void disableUser(Long id);

}
