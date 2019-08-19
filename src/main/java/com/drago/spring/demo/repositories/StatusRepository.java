package com.drago.spring.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drago.spring.demo.domain.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {

	Status findByStatusCode(Integer integer);

}
