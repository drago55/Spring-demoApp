package com.drago.spring.demo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class LatLon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Float lat;
    private Float lon;

    public LatLon(){}

    public LatLon(Float lat, Float lon) {
        this.lat=lat;
        this.lon=lon;
    }
}
