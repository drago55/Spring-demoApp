package com.drago.spring.demo.repositories;

import com.drago.spring.demo.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {


}
