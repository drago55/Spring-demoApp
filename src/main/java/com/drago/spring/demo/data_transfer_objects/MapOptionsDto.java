package com.drago.spring.demo.data_transfer_objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MapOptionsDto {
	
	private Float lat;

	private Float lon;
	
	private Integer zoom;

	private Integer mapResetTime;

}
