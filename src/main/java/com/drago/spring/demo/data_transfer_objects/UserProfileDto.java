package com.drago.spring.demo.data_transfer_objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserProfileDto {
	
	private Long id;
	
	private Long userId;

	private MapOptionsDto mapOptions;

}
