package com.drago.spring.demo.services.servicesimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.drago.spring.demo.domain.VerificationToken;
import com.drago.spring.demo.domain.Status;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.enums.RoleEnum;
import com.drago.spring.demo.enums.StatusEnum;
import com.drago.spring.demo.exception.InvalidUserException;
import com.drago.spring.demo.exception.UserNotFoundException;
import com.drago.spring.demo.repositories.VerificatinTokenRepository;
import com.drago.spring.demo.repositories.RoleRepository;
import com.drago.spring.demo.repositories.UserRepository;
import com.drago.spring.demo.services.StatusService;
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

	@Autowired
	private VerificatinTokenRepository passwordResetToken;

	@Autowired
	private StatusService statusService;

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
		Status userStatus = statusService.getStatusByCode(StatusEnum.INACTIVE);
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
			throw new UserNotFoundException("Username or Password is wrong!!");
		}

		if (!passwordEncoder.matches(userLoginDto.getUserPassword(), userOptional.get().getPassword())) {
			throw new InvalidUserException("Username or Password is wrong!!");
		}

		return true;
	}

	@Override
	@Transactional
	public void createVerificationTokenForUser(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		passwordResetToken.save(myToken);
	}

	@Override
	public List<UserDto> findAll() {
		return ObjectMapperUtils.mapAll(userRepository.findAll(), UserDto.class);
	}

	@Override
	public User findUserById(Long id) {
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
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("User does not exists");
		}

		User user = optionalUser.get();

		if (user.equals(getAuthenticatedUser())) {
			throw new IllegalStateException("Can't disable current user!");
		}

		if (user.getStatus().getStatusCode().equals(StatusEnum.ACTIVE.getStatusCode())) {

			user.setStatus(statusService.getStatusByCode(StatusEnum.INACTIVE));

		} else if (user.getStatus().getStatusCode().equals(StatusEnum.INACTIVE.getStatusCode())) {

			user.setStatus(statusService.getStatusByCode(StatusEnum.ACTIVE));
		}

		userRepository.save(user);

	}

	@Override
	public void logoutUser(HttpServletRequest request) {
		log.info("Logging out user...");
		HttpSession session = request.getSession();
		session.invalidate();
		SecurityContextHolder.clearContext();
	}

	@Override
	public void changeUserPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

}
