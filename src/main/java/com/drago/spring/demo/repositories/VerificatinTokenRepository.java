package com.drago.spring.demo.repositories;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.drago.spring.demo.domain.VerificationToken;

public interface VerificatinTokenRepository extends JpaRepository<VerificationToken, Long>{

	VerificationToken findByToken(String token);

	void deleteByToken(String token);

	@Modifying
	@Query("delete from VerificationToken t where t.expiryDate <= ?1")
	void deleteAllExpiredTokens(Timestamp now);

}
