package com.drago.spring.demo.domain;

import com.drago.spring.demo.constraint.FieldMatch;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@FieldMatch.List({
        @FieldMatch(first="email",second = "confirmEmail", message = "Email must match!"),
        @FieldMatch(first="password", second="confirmPassword",message = "Password don't match!")
})
@Data
public class UserRegistrationDto {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Email
    private String confirmEmail;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;


}
