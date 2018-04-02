package com.drago.spring.demo.data_transfer_objects;

import lombok.Data;

import java.util.Set;

@Data
public class MarkerDto {

    private String markerName;

    private String markerType;

    private String description;

    private Set<String> images;

    private Float lat;

    private Float lon;

    private String createdByUser;

    private String city;

    private String state;

    private String address;

    private String postalCode;
}