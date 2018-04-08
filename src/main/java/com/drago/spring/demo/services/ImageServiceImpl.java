package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    private StorageService storageService;

    @Override
    public Path uploadImage(MultipartFile multipartFile) {
        return storageService.store(multipartFile);
    }

    @Override
    public void deleteAllImage() {
        storageService.deleteAll();
    }

    @Override
    public List<MultipartFile> filterAndGetCollection(MultipartFile[] files) {
        return Stream.of(files).filter((multipartFile) -> !multipartFile.isEmpty()).collect(Collectors.toList());
    }


    @Override
    public void deleteImage(Image image) {
        log.debug("deleting image " + image.getImagePath());
        storageService.deleteFile(image.getImagePath());
    }


    @Override
    public void uploadImages(List<MultipartFile> files) {
        files.forEach((multipartFile -> storageService.store(multipartFile)));
    }

}
