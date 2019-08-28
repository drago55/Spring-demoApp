package com.drago.spring.demo.data_transfer_objects;

import javax.validation.constraints.NotEmpty;

import com.drago.spring.demo.constraint.FieldMatch;
import com.drago.spring.demo.constraint.ValidPassword;

import lombok.Data;

@FieldMatch(first = "password", second = "confirmPassword", message = "Password don't match!")
@Data
public class PasswordDto {

	@NotEmpty
	@ValidPassword
	private String password;
	@NotEmpty
	@ValidPassword
	private String confirmPassword;

}
