package com.drago.spring.demo.data_transfer_objects;

import java.util.Set;

import lombok.Data;

@Data
public class MarkerDto {

	private Long id;

	private String markerName;

	private String markerType;

	private String description;

	private Set<ImageDto> images;

	private Float lat;

	private Float lon;

	private String userFirstName;

	private String userLastName;

	private String city;

	private String state;

	private String address;

	private String postalCode;
}
