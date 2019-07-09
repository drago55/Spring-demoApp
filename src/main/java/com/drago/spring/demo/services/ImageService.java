package com.drago.spring.demo.services;

import java.nio.file.Path;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.drago.spring.demo.domain.Image;

public interface ImageService {

	Path uploadImage(MultipartFile multipartFile);

	void deleteAllImage();

	List<MultipartFile> filterAndGetCollection(MultipartFile[] files);

	void deleteImage(Image image);

	void uploadImages(List<MultipartFile> files);

}
