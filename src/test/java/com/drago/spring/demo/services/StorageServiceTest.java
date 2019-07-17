package com.drago.spring.demo.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.drago.spring.demo.ApplicationTests;
import com.drago.spring.demo.services.servicesimpl.StorageServiceImpl;

@Transactional
public class StorageServiceTest extends ApplicationTests {

	@Autowired
	private StorageServiceImpl service;

	@Test
	public void testUploadImage() {
		// When
		MockMultipartFile[] file = { new MockMultipartFile("NewUpdateImage-539e-4239-a9fb-43749ddafb61.jpg",
				"NewImage.jpg", null, "bar".getBytes()) };
		service.setUserUploadLocation(Paths.get("drago"));

		// Then
		Path path = service.store(file[0]);
		assertThat(path).isNotNull();
		assertThat(path.getName(0).toString()).isEqualTo("upload-dir");
		assertThat(path.getName(1).toString()).isEqualTo("drago");
		assertThat(path.getNameCount()).isEqualTo(3);

	}

}
