package com.drago.spring.demo.services;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.drago.spring.demo.domain.VerificationToken;

public interface SecurityService {
	
	void sendResetPasswordMail(String contextPath, String userEmail);
	
	String validateVerificationToken(long id, String token);
	
	void deleteToken(String token);
	
	VerificationToken getVerificationToken(String token);

	void sendVerificationTokenMail(String contextPath, String userEmail);
	
	void grantUserAutority(Long id, List<GrantedAuthority> autorities);

}
