package com.drago.spring.demo.data_transfer_objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MapOptionsDto {
	
	private Long id;
	
	private LatLonDto latLon;
	
	private Integer zoom;

	private Integer resetTime;

}
