package com.drago.spring.demo.data_transfer_objects;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	private String lastName;
	private String firstName;
	private String email;
	private long id;
	private String statusDescription;
	private Set<RoleDto> roles;

}
