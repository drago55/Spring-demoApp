package com.drago.spring.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drago.spring.demo.domain.PasswordResetToken;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long>{

	PasswordResetToken findByToken(String token);

}
