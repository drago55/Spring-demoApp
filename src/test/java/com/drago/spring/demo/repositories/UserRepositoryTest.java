package com.drago.spring.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectRetrievalFailureException;

import com.drago.spring.demo.ApplicationTests;
import com.drago.spring.demo.domain.User;

@DataJpaTest
public class UserRepositoryTest extends ApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testGetById() {

		// When
		userRepository.save(getUser());
		// Get user from DB
		User userFromDb = userRepository.getOne(getUser().getId());
		assertThat(userFromDb).isEqualTo(getUser());
	}

	@Test
	@Transactional
	public void testGetUserByEmail() {
		// When
		userRepository.save(getUser());

		// Get user from DB
		User userFromDb = userRepository.findUserByEmail(getUser().getEmail()).get();

		assertThat(userFromDb).isEqualTo(getUser());
		assertThat(userFromDb.getEmail()).isEqualTo(getUser().getEmail());
	}

	@Test
	public void testGetAllUsers() {
		// when
		User user2 = getUser();
		user2.setEmail("drugi@net.hr");
		user2.setId(2l);
		user2.setPassword("BLa2sdsadsTEsts");
		userRepository.save(user2);
		// then
		List<User> listOfUsers = userRepository.findAll();
		assertThat(listOfUsers.size()).isEqualTo(2);
		assertThat(listOfUsers.get(1).getId()).isEqualTo(user2.getId());
		// Clean user 2
		userRepository.delete(user2);
	}

	@Test
	public void testDeleteUser() {
		// When
		User user = userRepository.save(getUser());
		// Then
		userRepository.delete(user);
		Optional<User> optionalOfUser = userRepository.findById(getUser().getId());
		assertThat(optionalOfUser).isEmpty();
		assertThatThrownBy(() -> userRepository.getOne(getUser().getId())).isInstanceOf(ObjectRetrievalFailureException.class);
	}

	private static User getUser() {
		User user = new User();
		user.setId(1l);
		user.setFirstName("Dragutin");
		user.setLastName("Horvat");
		user.setPassword("$2a$10$e2YQ8XsCKkfCDhVYH/VxA.cA36bNVZjftSviUgs6hrl/Gw5jSrswe");
		user.setEmail("drago@net.hr");

		return user;
	}

}
