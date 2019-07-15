package com.drago.spring.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.drago.spring.demo.ApplicationTests;
import com.drago.spring.demo.data_transfer_objects.UserDto;
import com.drago.spring.demo.data_transfer_objects.UserLoginDto;
import com.drago.spring.demo.data_transfer_objects.UserRegistrationDto;
import com.drago.spring.demo.domain.Role;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.exception.InvalidUserException;
import com.drago.spring.demo.services.servicesimpl.UserServiceImpl;

@Transactional
public class UserServiceTest extends ApplicationTests {

	@Autowired
	private UserServiceImpl userService;

	@Test
	public void testFindUserByEmail() {
		assertThat(userService.findUserByEmail("drago@net.hr")).isNotNull();
	}

	@Test
	public void testFindUserByEmail_ThrowUsernameNotFoundException() {
		UserLoginDto user = getUserLoginDto();
		user.setUserEmail("unknown@net.hr");
		assertThatThrownBy(() -> userService.findUserByEmail(user.getUserEmail()))
				.isInstanceOf(UsernameNotFoundException.class).hasMessage("User not found!");
	}

	@Test
	public void testLoadUserByUsername() {
		UserDetails userDetails = userService.loadUserByUsername(getUserLoginDto().getUserEmail());
		assertThat(userDetails).isNotNull();
		assertThat(userDetails.getUsername()).isEqualTo(getUserLoginDto().getUserEmail());
	}

	@Test
	public void testLoadUserByUsername_ThrowUsernameNotFoundException() throws UsernameNotFoundException {
		UserLoginDto user = getUserLoginDto();
		user.setUserEmail("unknown@net.hr");
		assertThatThrownBy(() -> userService.loadUserByUsername(user.getUserEmail()))
				.isInstanceOf(UsernameNotFoundException.class).hasMessage("User does not exists");
	}

	@Test
	public void testSave() {
		User createdUser = userService.save(getUserRegistrationDto());
		assertThat(createdUser.getEmail()).isSameAs(getUserRegistrationDto().getEmail());
		assertThat(userService.findUseById(createdUser.getId())).isNotNull();

	}

	@Test
	public void testIsValidUser() throws InvalidUserException {
		assertThat(userService.isValidUser(getUserLoginDto())).isEqualTo(true);
	}

	@Test
	public void test_ThrowUsernameNotFoundException() throws UsernameNotFoundException {
		UserLoginDto user = getUserLoginDto();
		user.setUserEmail("unknown@net.hr");
		assertThatThrownBy(() -> userService.isValidUser(user)).isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("Username or Password is wrong!!");
	}

	@Test
	public void test_InvalidUserException() throws InvalidUserException {
		UserLoginDto user = getUserLoginDto();
		user.setUserPassword("invalidpass");
		assertThatThrownBy(() -> userService.isValidUser(user)).isInstanceOf(InvalidUserException.class)
				.hasMessage("Username or Password is wrong!!");
	}

	@Test
	public void testfinedUserByUserEmail() {
		// When
		userService.save(getUserRegistrationDto());
		// Then
		User user = userService.findUserByEmail(getUserRegistrationDto().getEmail());
		assertThat(user.getEmail()).isSameAs(getUserRegistrationDto().getEmail());
	}

	@Test
	public void testLoadAllUsers() {
		List<UserDto> listOfUserDto = userService.findAll();
		assertThat(listOfUserDto).size().isEqualTo(1);
		assertThat(listOfUserDto).isNotNull();

	}

	@Test
	@WithMockUser(username = "drago@net.hr", authorities = { "ADMIN" })
	public void test_user_loged_in() {
		// When
		User user = userService.getAuthenticatedUser();
		// Then
		assertThat(user).isNotNull();
		assertThat(user.getRoles()).containsAll(getRoles());
		assertThat(user.getEmail()).isEqualTo(getUserLoginDto().getUserEmail());

	}

	private UserRegistrationDto getUserRegistrationDto() {

		UserRegistrationDto user = new UserRegistrationDto();
		user.setEmail("test@net.hr");
		user.setConfirmEmail("test@net.hr");
		user.setFirstName("TestUser");
		user.setLastName("TestUser");
		user.setPassword("123456");
		user.setConfirmPassword("123456");

		return user;
	}

	private UserLoginDto getUserLoginDto() {
		UserLoginDto user = new UserLoginDto();
		user.setUserEmail("drago@net.hr");
		user.setUserPassword("123456");
		return user;
	}

	private Set<Role> getRoles() {

		Set<Role> listOfRoles = new HashSet<>();
		Role roleAdmin = new Role();
		roleAdmin.setId(2l);
		roleAdmin.setName("ROLE_ADMIN");
		listOfRoles.add(roleAdmin);

		return listOfRoles;
	}
}