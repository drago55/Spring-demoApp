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

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		return emailExist(email);
	}

	private boolean emailExist(String email) {
		return !userRepository.findUserByEmail(email).isPresent();
	}

}
