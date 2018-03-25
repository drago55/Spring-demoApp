package com.drago.spring.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String fileName;

    @Lob
    private String image;

    @ManyToMany(mappedBy = "images",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<User> users;

    @ManyToMany(mappedBy = "images",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Marker> markers;
    /**
     * Name and  String Base64 image
     * @param fileName
     * @param image
     */
    public Image(String fileName, String image) {

        this.fileName = fileName;
        this.image = image;
    }

    public Image(){}
}
