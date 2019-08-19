package com.drago.spring.demo.services.servicesimpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drago.spring.demo.ObjectMapperUtils;
import com.drago.spring.demo.data_transfer_objects.MarkerDto;
import com.drago.spring.demo.domain.Image;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.Status;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.enums.StatusEnum;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import com.drago.spring.demo.repositories.MarkerRepository;
import com.drago.spring.demo.repositories.StatusRepository;
import com.drago.spring.demo.services.MarkerService;
import com.drago.spring.demo.services.StorageService;
import com.drago.spring.demo.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MarkerServiceImpl implements MarkerService {

	@Autowired
	private MarkerRepository markerRepository;

	@Autowired
	private StorageService storageService;

	@Autowired
	private UserService userService;

	@Autowired
	private StatusRepository statusRepository;

	@Override
	public MarkerDto save(MarkerDto markerDto, MultipartFile[] files) {

		Marker marker = ObjectMapperUtils.map(markerDto, Marker.class);

		storageService.setUserUploadLocation(Paths.get(userService.getAuthenticatedUser().getLastName()));

		User user = userService.getAuthenticatedUser();

		marker.setUser(user);

		List<MultipartFile> listOfImages = filterAndGetListOfImages(files);

		uploadImagesAndSetPaths(marker, listOfImages);

		if (marker.getId() != null) {
			log.debug("it is update resolve orphan images!");
			log.debug("-------------paths of new marker images-------------");
			marker.getImages().forEach(image -> log.debug(Paths.get(image.getImagePath()).getFileName().toString()));
			log.debug("-----------------paths of old marker images---------");
			markerRepository.getOne(marker.getId()).getImages().forEach(image -> log.debug((Paths.get(image.getImagePath()).getFileName().toString())));
		}

		return ObjectMapperUtils.map(markerRepository.save(marker), MarkerDto.class);
	}

	private List<MultipartFile> filterAndGetListOfImages(MultipartFile[] files) {
		return Stream.of(files).filter(multipartFile -> !multipartFile.isEmpty()).collect(Collectors.toList());
	}

	private void uploadImagesAndSetPaths(Marker marker, List<MultipartFile> listOfFiles) {
		marker.setImages(new HashSet<>());
		listOfFiles.forEach(multipartFile -> {
			Path path = storageService.store(multipartFile);
			marker.addImage(new Image(multipartFile.getOriginalFilename(), path.toString()));
		});
	}

	@Override
	public MarkerDto findMarkerById(Long id) {
		if (!markerRepository.existsById(id)) {
			throw new NoSuchMarkerException("Marker don't exists!");
		}
		return ObjectMapperUtils.map(markerRepository.getOne(id), MarkerDto.class);
	}

	@Override
	public Optional<MarkerDto> findMarkerByUserId(Long userId) {
		return Optional.of(ObjectMapperUtils.map(markerRepository.getOne(userId), MarkerDto.class));
	}

	@Override
	public Set<MarkerDto> findAllMarkers() {
		Status markerStatus = statusRepository.findByStatusCode(StatusEnum.ACTIVE.getStatusCode());
		List<Marker> markers =markerRepository.findAllByStatus(markerStatus);
		return new HashSet<>(ObjectMapperUtils.mapAll(markers, MarkerDto.class));
	}

	@Override
	public Page<MarkerDto> findPaginatedMarkers(Pageable pageable) {
		Status markerStatus = statusRepository.findByStatusCode(StatusEnum.ACTIVE.getStatusCode());
		Page<Marker> page = markerRepository.findAllByStatus(pageable, markerStatus);
		return new PageImpl<>(ObjectMapperUtils.mapAll(page.getContent(), MarkerDto.class), pageable, page.getTotalElements());
	}

	@Override
	public void deleteMarkerById(Long id) {
		Status markerStatus = statusRepository.findByStatusCode(StatusEnum.INACTIVE.getStatusCode());
		Optional<Marker> markerOptional = markerRepository.findById(id);
		if (markerOptional.isPresent()) {
			Marker marker = markerOptional.get();
			marker.setStatus(markerStatus);
			markerRepository.save(marker);
		}

	}

}
