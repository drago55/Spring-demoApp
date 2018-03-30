package com.drago.spring.demo.services;

import com.drago.spring.demo.exception.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

    private final Path uploadLocation = Paths.get("src/main/resources/static/image/upload-dir");

    private Path userNameDir;

    public StorageServiceImpl() {
    }

    public StorageServiceImpl(Path userNameDir) {
        this.userNameDir = userNameDir;
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(uploadLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create upload directory!!");
        }
    }

    @Override
    public Path setUserDir(Path path) {

        this.userNameDir = uploadLocation.resolve(path);
        try {
            Files.createDirectories(userNameDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userNameDir;
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (userNameDir == null) {
            throw new RuntimeException("Set user upload directory!");
        }
        try {
            if (filename.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException("Cannot store file with relative path outside current directory "
                        + filename);
            }
            Files.copy(file.getInputStream(), this.userNameDir.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file!" + e);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.userNameDir, 1)
                    .filter(path -> !path.equals(this.userNameDir))
                    .map(path -> this.userNameDir.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = userNameDir.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Fail resource doesn't exist or its not readable!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(uploadLocation.toFile());
    }
}
