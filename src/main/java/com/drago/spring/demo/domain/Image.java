package com.drago.spring.demo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Blob;
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

    public Image(String fileName, String image) {

        this.fileName = fileName;
        this.image = image;
    }

    public Image() {
    }
}
