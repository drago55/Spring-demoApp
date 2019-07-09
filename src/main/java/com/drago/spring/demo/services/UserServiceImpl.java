package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.data_transfer_objects.UserLoginDto;
import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
import com.drago.spring.demo.exception.EmailExistsException;
import com.drago.spring.demo.exception.InvalidUserException;
import com.drago.spring.demo.repositories.RoleRepository;
import com.drago.spring.demo.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public User findUserByEmail(String email) {

		Optional<User> userOptional = userRepository.findUserByEmail(email);

		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException("User not found!");
		}

		return userOptional.get();
	}

	@Override
	@Transactional
	public User save(UserRegistrationDto userRegistrationDto) {

		User user = new User();
		user.setFirstName(userRegistrationDto.getFirstName());
		user.setLastName(userRegistrationDto.getLastName());
		user.setEmail(userRegistrationDto.getEmail());
		user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

		user.setRoles(new HashSet<>(roleRepository.findAll()));

		if (emailExist(userRegistrationDto.getEmail())) {
			throw new EmailExistsException("Error email address already exists!");
		}

		return userRepository.save(user);
	}

	@Override
	public boolean isValidUser(UserLoginDto userLoginDto) throws InvalidUserException {

		Optional<User> userOptional = userRepository.findUserByEmail(userLoginDto.getUserEmail());

		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException("Username or Password is wrong!!");
		}

		if (!passwordEncoder.matches(userLoginDto.getUserPassword(), userOptional.get().getPassword())) {
			throw new InvalidUserException("Username or Password is wrong!!");
		}

		return true;
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User findUseById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return this.findUserByEmail(authentication.getName());
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) {
		Optional<User> userOptional = userRepository.findUserByEmail(email);
		if (!userOptional.isPresent()) {
			throw new UsernameNotFoundException("User does not exists");
		}
		User user = userOptional.get();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		user.getRoles().forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				grantedAuthorities);
	}

	private boolean emailExist(String email) {
		return userRepository.findUserByEmail(email).isPresent();
	}

}
