package com.drago.spring.demo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.drago.spring.demo.ApplicationTests;
import com.drago.spring.demo.domain.Role;
import com.drago.spring.demo.enums.RoleEnum;

@DataJpaTest
public class RolesRepositoryTest extends ApplicationTests {

	@Autowired
	private RoleRepository roleRepository;
	
	@Test
	public void test_FindAll() {
		// Then 
		List<Role> roles = roleRepository.findAll();
		assertThat(roles).isNotNull();
		assertThat(roles).containsExactlyElementsOf(roles);
	}

	@Test
	public void test_FindAllByName() {
		// Then 		
		Set<Role> roles = roleRepository.findAllByName(RoleEnum.ADMIN.getName());
		assertThat(roles).containsOnly(getRoles().get(1));
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
