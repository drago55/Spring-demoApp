package com.drago.spring.demo.data_transfer_objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class LatLonDto {
	
	private Long id;
	
	private Float lat;

	private Float lon;

}
