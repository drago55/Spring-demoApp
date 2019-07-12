package com.drago.spring.demo.services.servicesimpl;

import com.drago.spring.demo.ObjectMapperUtils;
import com.drago.spring.demo.data_transfer_objects.MarkerDto;
import com.drago.spring.demo.domain.Image;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import com.drago.spring.demo.repositories.MarkerRepository;
import com.drago.spring.demo.services.ImageService;
import com.drago.spring.demo.services.MarkerService;
import com.drago.spring.demo.services.StorageService;
import com.drago.spring.demo.services.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class MarkerServiceImpl implements MarkerService {

	@Autowired
	private MarkerRepository markerRepository;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private UserService userService;

	@Override
	public MarkerDto save(MarkerDto markerDto, MultipartFile[] files) {
		
		Marker marker = ObjectMapperUtils.map(markerDto, Marker.class);

		storageService.setUserDir(Paths.get(userService.getAuthenticatedUser().getLastName()));

		User user = userService.getAuthenticatedUser();

		marker.setUser(user);
		
		List<MultipartFile> listOfImages = imageService.filterAndGetCollection(files);

		uploadImages(marker, listOfImages);

		if (marker.getId() != null) {
			log.debug("it is update resolve orphan images!");
			log.debug("-------------paths of new marker images-------------");
			marker.getImages().forEach(image -> log.debug(Paths.get(image.getImagePath()).getFileName().toString()));
			log.debug("-----------------paths of old marker images---------");
			markerRepository.getOne(marker.getId()).getImages()
					.forEach(image -> log.debug((Paths.get(image.getImagePath()).getFileName().toString())));
		}

		return ObjectMapperUtils.map(markerRepository.save(marker), MarkerDto.class);
	}

	private void uploadImages(Marker marker, List<MultipartFile> listOfFiles) {
		marker.setImages(new HashSet<>());
		listOfFiles.forEach(multipartFile -> {
			Path path = imageService.uploadImage(multipartFile);
			marker.addImage(new Image(multipartFile.getOriginalFilename(), path.toString()));
		});
	}

	@Override
	public MarkerDto findMarkerById(Long id) {
		if (!markerRepository.exists(id)) {
			throw new NoSuchMarkerException("Marker don't exists!");
		}
		return ObjectMapperUtils.map(markerRepository.findOne(id), MarkerDto.class);
	}

	@Override
	public Optional<MarkerDto> findMarkerByUserId(Long userId) {
		return Optional.of(ObjectMapperUtils.map(markerRepository.getOne(userId), MarkerDto.class));
	}

	@Override
	public Set<MarkerDto> findAllMarkers() {
		return new HashSet<>(ObjectMapperUtils.mapAll(markerRepository.findAll(), MarkerDto.class));
	}

	@Override
	public void deleteMarkerById(Long id) {
		markerRepository.delete(id);
	}

}
