package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ImageService {


    Path uploadImage(MultipartFile multipartFile);

    void deleteAllImage();

    List<MultipartFile> filterAndGetCollection(MultipartFile[] files);

    void deleteImage(Image image);


    void uploadImages(List<MultipartFile> files);


}
