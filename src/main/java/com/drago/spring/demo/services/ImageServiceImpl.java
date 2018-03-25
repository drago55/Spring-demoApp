package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public Image save(Image image) {
        return null;
    }

    @Override
    public List<Image> findAllImages() {
        return null;
    }
/*
    @Autowired
    private ImageRepository imageRepository;


    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public List<Image> findAllImages() {
        return null;
    }
*/
}
