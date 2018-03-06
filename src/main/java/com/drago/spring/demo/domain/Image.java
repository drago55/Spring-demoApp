package com.drago.spring.demo.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Byte[] image;

    @ManyToOne
    private Marker marker;
    @ManyToOne
    private User user;

}
