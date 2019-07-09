package com.drago.spring.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drago.spring.demo.domain.Marker;

public interface MarkerRepository extends JpaRepository<Marker, Long> {

	Set<Marker> findByUserId(Long id);

}
