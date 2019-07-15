package com.drago.spring.demo.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.drago.spring.demo.ApplicationTests;

@DataJpaTest
public class MarkerRepositoryTest extends ApplicationTests {

	@Autowired
	private MarkerRepository markerRepository;

}
