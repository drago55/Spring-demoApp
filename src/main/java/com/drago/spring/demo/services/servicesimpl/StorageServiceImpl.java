package com.drago.spring.demo.services.servicesimpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.drago.spring.demo.exception.StorageException;
import com.drago.spring.demo.services.StorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

	@Value("${upload.path}")
	private String path;

	private Path rootUploadLocation;

	private Path userUploadLocation;

	public StorageServiceImpl() {
	}

	public StorageServiceImpl(Path storedLocation) {
		this.userUploadLocation = storedLocation;
	}

	@Override
	@PostConstruct
	public void init() {
		try {
			rootUploadLocation = Paths.get(path);
			Files.createDirectories(rootUploadLocation);
		} catch (IOException e) {
			log.error("Failed to create upload directory!! ", e);
			throw new StorageException("Failed to create upload directory!!");
		}
	}

	@Override
	public Path setUserUploadLocation(Path path) {

		this.userUploadLocation = rootUploadLocation.resolve(path);
		try {
			Files.createDirectories(userUploadLocation);
		} catch (IOException e) {
			log.error("Failed to create upload directory for specific user " + path.toString(), e);
		}

		return userUploadLocation;
	}

	@Override
	public Path store(MultipartFile file) {

		String filename = getFileName(file);

		if (this.userUploadLocation == null) {
			throw new StorageException("Can't upload to root location! Set upload location for user!");
		}

		Path path = this.userUploadLocation.resolve(filename);

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
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.userUploadLocation, 1).filter(path -> !path.equals(this.userUploadLocation))
					.map(path -> this.userUploadLocation.relativize(path));
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
			Path file = userUploadLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageException("Fail resource doesn't exist or its not readable!");
			}
		} catch (MalformedURLException e) {
			throw new StorageException("FAIL!", e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootUploadLocation.toFile());
	}

	@Override
	public void deleteFile(String fileName) {
		final Path path = userUploadLocation.resolve(fileName);
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

	public String getPath() {
		return path;
	}

	public Path getRootUploadLocation() {
		return rootUploadLocation;
	}

	public Path getUserUploadLocation() {
		return userUploadLocation;
	}

}
