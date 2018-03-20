package com.drago.spring.demo.repositories;

import com.drago.spring.demo.domain.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkerRepositry extends JpaRepository<Marker,Long>{

}
