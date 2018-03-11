package com.drago.spring.demo.domain;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NonNull
    @NotEmpty
    private String userName;

    @NonNull
    @NotEmpty
    private String email;

    @NonNull
    @NotEmpty
    private String password;


    private String newPassword;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Image> images= new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Marker> markers= new HashSet<>();

    public User(){}
}
