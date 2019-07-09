package com.drago.spring.demo.services;

import com.drago.spring.demo.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

	private final Path uploadLocation = Paths.get("/upload-dir");

	private Path storedLocation;

	private List<String> fileNames = new ArrayList<>();

	public StorageServiceImpl() {
	}

	public StorageServiceImpl(Path storedLocation) {
		this.storedLocation = storedLocation;
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(uploadLocation);
		} catch (IOException e) {
			log.error("Failed to create upload directory!! ", e);
			throw new RuntimeException("Failed to create upload directory!!");
		}
	}

	@Override
	public Path setUserDir(Path path) {

		this.storedLocation = uploadLocation.resolve(path);
		try {
			Files.createDirectories(storedLocation);
		} catch (IOException e) {
			log.error("Failed to create upload directory for specific user " + path.toString(), e);
		}

		return storedLocation;
	}

	@Override
	public Path store(MultipartFile file) {

		String filename = getFileName(file);
		Path path = this.storedLocation.resolve(filename);
		
		try {
			if (filename.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			log.debug("path to be stored " + path);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			return path;

		} catch (IOException e) {
			throw new StorageException("Failed to store file!" + e);
		}

	}

	@Override
	public String getFileName(MultipartFile file) {
		return StringUtils.cleanPath(
				UUID.randomUUID().toString() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
	}

	@Override
	public List<String> getFileNames() {
		return this.fileNames;
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.storedLocation, 1).filter(path -> !path.equals(this.storedLocation))
					.map(path -> this.storedLocation.relativize(path));
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
			Path file = storedLocation.resolve(filename);
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

	@Override
	public void deleteFile(String fileName) {
		final Path path = storedLocation.resolve(fileName);
		try {
			Files.delete(path);
		} catch (NoSuchFileException x) {
			log.error("%s: no such" + " file or directory%n", path);
		} catch (DirectoryNotEmptyException x) {
			log.error("%s not empty%n", path);
		} catch (IOException x) {
			// File permission problems are caught here.
			log.error("Exception while trying to delete file" + x);
		}
	}

}
