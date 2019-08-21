package com.drago.spring.demo.services;

public interface SecurityService {
	
	public void sendResetPassword(String contextPath, String userEmail);
	
	public String validatePasswordResetToken(long id, String token);

}
