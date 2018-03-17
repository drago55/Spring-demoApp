package com.drago.spring.demo.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(exclude = {"user","marker"})
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private Byte[] image;

    @ManyToOne
    private Marker marker;
    @ManyToOne
    private User user;

}
