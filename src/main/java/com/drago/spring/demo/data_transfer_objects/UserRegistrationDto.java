package com.drago.spring.demo.data_transfer_objects;

import com.drago.spring.demo.constraint.EmailNotInUse;
import com.drago.spring.demo.constraint.FieldMatch;
import com.drago.spring.demo.constraint.ValidPassword;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@FieldMatch.List({ @FieldMatch(first = "email", second = "confirmEmail", message = "Email must match!"),
		@FieldMatch(first = "password", second = "confirmPassword", message = "Password don't match!") })
@Data
public class UserRegistrationDto {
	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	@NotEmpty
	@Email
	@EmailNotInUse
	private String email;
	@NotEmpty
	@Email
	@EmailNotInUse
	private String confirmEmail;
	@NotEmpty
	@ValidPassword
	private String password;
	@NotEmpty
	@ValidPassword
	private String confirmPassword;

}
