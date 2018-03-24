package com.drago.spring.demo.repositories;

import com.drago.spring.demo.domain.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarkerRepository extends JpaRepository<Marker,Long>{

}
