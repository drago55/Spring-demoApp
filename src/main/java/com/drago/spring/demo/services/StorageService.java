package com.drago.spring.demo.services;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    Path setUserDir(Path path);

    Path store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void deleteFile(String fileName);

    String getFileName(MultipartFile file);

    List<String> getFileNames();

}
