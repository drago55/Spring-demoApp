package com.drago.spring.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"user","images","latLon","location"})
public class Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String description;
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    private LatLon latLon;

    @OneToOne(fetch = FetchType.EAGER)
    private Location location;

    private MarkerType markerType;

    @ManyToMany
    private Set<User> user= new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marker")
    private Set<Image> images = new HashSet<>();

}
