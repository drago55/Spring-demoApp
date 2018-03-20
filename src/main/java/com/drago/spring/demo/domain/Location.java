package com.drago.spring.demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String streetAddress;
    private String postalCode;
    private String city;
    private String stateProvince;

    public Location(){}

    public Location(String city, String postalCode, String stateProvince, String streetAddress) {
            this.city=city;
            this.postalCode=postalCode;
            this.stateProvince=stateProvince;
            this.streetAddress=streetAddress;
    }
}
