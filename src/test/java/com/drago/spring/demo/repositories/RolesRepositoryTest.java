package com.drago.spring.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.drago.spring.demo.domain.Role;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RolesRepositoryTest {

	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void testSaveRoles() {
		// When
		List<Role> insertedRoles = roleRepository.save(getRoles());

		assertThat(insertedRoles).isNotNull();
	}

	@Test
	public void testGetAllRoles() {
		List<Role> roles =roleRepository.findAll();
		assertThat(roles).containsExactlyElementsOf(getRoles());
	}

	private List<Role> getRoles() {

		List<Role> listOfRoles = new ArrayList<>();
		Role roleUser = new Role();
		roleUser.setId(1l);
		roleUser.setName("ROLE_USER");
		Role roleAdmin = new Role();
		roleAdmin.setId(2l);
		roleAdmin.setName("ROLE_ADMIN");
		listOfRoles.add(roleUser);
		listOfRoles.add(roleAdmin);

		return listOfRoles;
	}
}
