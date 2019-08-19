package com.drago.spring.demo.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.Status;

public interface MarkerRepository extends JpaRepository<Marker, Long> {

	Set<Marker> findByUserId(Long id);

	Page<Marker> findAllByStatus(Pageable pageable, Status markerStatus);

	List<Marker> findAllByStatus(Status markerStatus);

}
