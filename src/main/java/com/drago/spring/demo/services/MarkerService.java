package com.drago.spring.demo.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.drago.spring.demo.data_transfer_objects.MarkerDto;

public interface MarkerService {

	MarkerDto save(MarkerDto marker, MultipartFile[] multipartFile);

	MarkerDto findMarkerById(Long id);

	Optional<MarkerDto> findMarkerByUserId(Long userId);

	Set<MarkerDto> findAllMarkers();

	void deleteMarkerById(Long id);
	
	Page<MarkerDto> findPaginatedMarkers(Pageable pageable);
	
}
