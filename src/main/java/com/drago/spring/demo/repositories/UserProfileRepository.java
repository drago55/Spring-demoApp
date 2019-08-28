package com.drago.spring.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drago.spring.demo.domain.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

	Optional<UserProfile> findByUserId(Long id);

}
