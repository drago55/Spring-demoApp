package com.drago.spring.demo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Marker{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    private LatLon latLon;

    @OneToOne(fetch = FetchType.EAGER)
    private Location location;

    private MarkerType markerType;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marker")
    private Set<Image> images= new HashSet<>();

}
