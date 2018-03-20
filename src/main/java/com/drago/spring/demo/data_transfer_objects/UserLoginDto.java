package com.drago.spring.demo.data_transfer_objects;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class UserLoginDto {

    @NotEmpty
    @Email
    private String userEmail;

    @NotEmpty
    private String userPassword;
}
