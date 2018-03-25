package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Image;

import java.util.List;

public interface ImageService {

    Image save(Image image);

    List<Image> findAllImages();
}
