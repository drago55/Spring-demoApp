package com.drago.spring.demo.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.drago.spring.demo.repositories.UserRepository;

public class EmailNotInUseValidator implements ConstraintValidator<EmailNotInUse, String> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void initialize(EmailNotInUse constraintAnnotation) {
	}
	
	/**
	 * Email validation 
	 */
	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return !isEmailPresent(email);
	}

	private boolean isEmailPresent(String email) {
		return userRepository.findUserByEmail(email).isPresent();
	}

}
