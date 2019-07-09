package com.drago.spring.demo.data_transfer_objects;

import java.util.Set;

import lombok.Data;

@Data
public class MarkerDto {

	private String markerName;

	private String markerType;

	private String description;

	private Set<String> imagePaths;

	private Float lat;

	private Float lon;

	private String createdByUser;

	private String city;

	private String state;

	private String address;

	private String postalCode;
}
