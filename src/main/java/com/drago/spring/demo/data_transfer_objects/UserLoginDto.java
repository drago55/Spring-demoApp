package com.drago.spring.demo.data_transfer_objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserLoginDto {

    @NotEmpty
    @Email
    private String userEmail;

    @NotEmpty
    private String userPassword;
}
