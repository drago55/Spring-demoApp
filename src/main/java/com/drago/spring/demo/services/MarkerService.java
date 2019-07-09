package com.drago.spring.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.drago.spring.demo.domain.Marker;

public interface MarkerService {

    Marker save(Marker marker, MultipartFile[] multipartFile);

    Marker findMarkerById(Long id);

    Optional<Marker> findMarkerByUserId(Long userId);

    List<Marker> findAllMarkers();

    void deleteMarkerById(Long id);

}
