package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface MarkerService {

    Marker save(Marker marker, MultipartFile[] multipartFile);

    Marker findMarkerById(Long id);

    Optional<Marker> findMarkerByUserId(Long userId);

    List<Marker> findAllMarkers();

    void deleteMarkerById(Long id);

}
