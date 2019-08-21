package com.drago.spring.demo.services.servicesimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import com.drago.spring.demo.domain.PasswordResetToken;
import com.drago.spring.demo.domain.Status;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.enums.RoleEnum;
import com.drago.spring.demo.enums.StatusEnum;
import com.drago.spring.demo.exception.InvalidUserException;
import com.drago.spring.demo.exception.UserNotFoundException;
import com.drago.spring.demo.repositories.PasswordTokenRepository;
import com.drago.spring.demo.repositories.RoleRepository;
import com.drago.spring.demo.repositories.StatusRepository;
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
	private StatusRepository statusRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordTokenRepository passwordResetToken;

	@Override
	public User findUserByEmail(String email) {

		Optional<User> userOptional = userRepository.findUserByEmail(email);

		if (!userOptional.isPresent()) {
			throw new UserNotFoundException("User not found!");
		}

		return userOptional.get();
	}

	@Override
	@Transactional
	public User save(UserRegistrationDto userRegistrationDto) {
		Status userStatus = statusRepository.findByStatusCode(StatusEnum.ACTIVE.getStatusCode());
		User user = modelMapper.map(userRegistrationDto, User.class);
		user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAllByName(RoleEnum.USER.getName())));
		user.setStatus(userStatus);
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
	@Transactional
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetToken.save(myToken);
	}

	@Override
	public List<UserDto> findAll() {
		return ObjectMapperUtils.mapAll(userRepository.findAll(), UserDto.class);
	}

	@Override
	public User findUseById(Long id) {
		return userRepository.getOne(id);
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
		if (userNotPresentOrNotActive(userOptional)) {
			throw new UsernameNotFoundException("User does not exists");
		}
		User user = userOptional.get();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		user.getRoles().forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getName())));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
	}

	private boolean userNotPresentOrNotActive(Optional<User> userOptional) {
		return !userOptional.isPresent() || userOptional.get().getStatus().getStatusCode().equals(StatusEnum.INACTIVE.getStatusCode());
	}

	@Override
	public Page<UserDto> findPaginatedUsers(Pageable pageable) {
		Page<User> page = userRepository.findAll(pageable);
		return new PageImpl<>(ObjectMapperUtils.mapAll(page.getContent(), UserDto.class), pageable, page.getTotalElements());
	}

	@Override
	public void disableOrEnableUser(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		List<Status> listOfStatus = statusRepository.findAll();
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User does not exists");
		}

		User user = optionalUser.get();

		if (user.equals(getAuthenticatedUser())) {
			throw new IllegalStateException("Can't disable current user!");
		}

		if (user.getStatus().getStatusCode().equals(StatusEnum.ACTIVE.getStatusCode())) {

			user.setStatus(getStatusByCode(listOfStatus, StatusEnum.INACTIVE));

		} else if (user.getStatus().getStatusCode().equals(StatusEnum.INACTIVE.getStatusCode())) {

			user.setStatus(getStatusByCode(listOfStatus, StatusEnum.ACTIVE));
		}

		userRepository.save(user);

	}

	private Status getStatusByCode(List<Status> listOfStatus, StatusEnum statusEnum) {
		Optional<Status> optionaOfStatus = listOfStatus.stream().filter(status -> status.getStatusCode().equals(statusEnum.getStatusCode())).findFirst();
		if (!optionaOfStatus.isPresent()) {
			throw new IllegalStateException("Status doesn't exist in database!");
		}
		return optionaOfStatus.get();
	}

}
