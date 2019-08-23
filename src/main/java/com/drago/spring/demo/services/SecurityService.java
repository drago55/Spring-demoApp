package com.drago.spring.demo.services;

public interface SecurityService {
	
	public void sendResetPasswordMail(String contextPath, String userEmail);
	
	public String validatePasswordResetToken(long id, String token);
	
	void deleteToken(String token);

}
