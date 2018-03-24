package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.exception.NoSuchMarkerException;

import java.util.List;
import java.util.Optional;

public interface MarkerService {

    Marker save(Marker marker);

    Marker findMarkerById(Long id) throws NoSuchMarkerException;

    Optional<Marker> findMarkerByUserId(Long userId);

    List<Marker> findAllMarkers();

    void deleteMarkerById(Long id);

}
