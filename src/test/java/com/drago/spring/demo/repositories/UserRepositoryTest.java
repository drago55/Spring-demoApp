package com.drago.spring.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.drago.spring.demo.ApplicationTests;
import com.drago.spring.demo.domain.User;

@DataJpaTest
public class UserRepositoryTest extends ApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testSave() {

		assertThat(userRepository.save(getUser())).isEqualTo(getUser());
	}

	@Test
	@Transactional
	public void testGetById() {

		// Save user in DB
		User userSavedInDb = userRepository.save(getUser());

		// Get user from DB
		User userFromDb = userRepository.getOne(userSavedInDb.getId());
		assertThat(userSavedInDb).isEqualTo(userFromDb);

	}

	@Test
	@Transactional
	public void testGetUserByEmail() {
		// Save user in DB
		User userSavedInDb = userRepository.save(getUser());

		// Get user from DB
		User userFromDb = userRepository.findUserByEmail(userSavedInDb.getEmail()).get();

		assertThat(userSavedInDb).isEqualTo(userFromDb);
		assertThat(userFromDb.getEmail()).isEqualTo(userSavedInDb.getEmail());
	}

	@Test
	@Transactional
	public void testGetAllUsers() {
		// when
		userRepository.save(getUser());
		User user2 = getUser();
		user2.setEmail("drugi@net.hr");
		user2.setId(2l);
		user2.setPassword("BLa2sdsadsTEsts");
		userRepository.save(user2);
		// then
		List<User> listOfUsers = userRepository.findAll();
		assertThat(listOfUsers.size()).isEqualTo(2);
		assertThat(listOfUsers.get(1).getId()).isEqualTo(user2.getId());

	}

	@Test
	@Transactional
	public void testDeleteUser() {

		// When
		User savedUser = userRepository.save(getUser());
		assertThat(getUser().getId()).isEqualTo(savedUser.getId());
		// Then
		userRepository.delete(savedUser);
		assertThat(userRepository.getOne(savedUser.getId())).isNull();

	}

	private User getUser() {
		User user = new User();
		user.setId(1l);
		user.setFirstName("Dragutin");
		user.setLastName("Horvat");
		user.setPassword("$2a$10$e2YQ8XsCKkfCDhVYH/VxA.cA36bNVZjftSviUgs6hrl/Gw5jSrswe");
		user.setEmail("drago@net.hr");
		return user;
	}

}
