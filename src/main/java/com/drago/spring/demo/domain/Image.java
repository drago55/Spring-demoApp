package com.drago.spring.demo.domain;

import lombok.Data;
import org.springframework.core.io.Resource;


import javax.persistence.*;
import java.nio.file.Path;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String fileName;

    @Lob
    private String imagePath;


    /**
     * Image
     *
     * @param fileName  original name of image
     * @param imagePath path of image
     */
    public Image(String fileName, String imagePath) {

        this.fileName = fileName;
        this.imagePath = imagePath;
    }

    public Image() {
    }
}
