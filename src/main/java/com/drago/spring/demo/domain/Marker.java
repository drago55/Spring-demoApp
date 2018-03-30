package com.drago.spring.demo.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.core.io.Resource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(exclude = {"user", "images", "latLon", "location"})
public class Marker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String description;
    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private LatLon latLon;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Location location;


    private String markerType;

    @ManyToOne
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "marker_images", joinColumns = @JoinColumn(name = "marker_id"), inverseJoinColumns = @JoinColumn(name = "image_id"))
    @JsonManagedReference
    private Set<Image> images = new HashSet<>();


    public Marker addImage(Image image) {
        this.images.add(image);
        return this;
    }

}
