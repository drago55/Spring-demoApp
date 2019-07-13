package com.drago.spring.demo.services.servicesimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
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

import com.drago.spring.demo.ObjectMapperUtils;
import com.drago.spring.demo.data_transfer_objects.UserDto;
import com.drago.spring.demo.data_transfer_objects.UserLoginDto;
import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.exception.InvalidUserException;
import com.drago.spring.demo.repositories.RoleRepository;
import com.drago.spring.demo.repositories.UserRepository;
import com.drago.spring.demo.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

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
		User user = modelMapper.map(userRegistrationDto, User.class);
		user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));
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
	public List<UserDto> findAll() {
		return ObjectMapperUtils.mapAll(userRepository.findAll(), UserDto.class);
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

}
