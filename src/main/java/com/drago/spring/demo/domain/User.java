package com.drago.spring.demo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String userName;
    private String email;
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Image> images= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Marker> markers= new HashSet<>();
}
