package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Marker;

import java.util.List;
import java.util.Optional;

public interface MarkerService {

    Marker save(Marker marker);

    Optional<Marker> findMarkerById(Long id);

    Optional<Marker> findMarkerByUserId(Long userId);

    List<Marker> findAllMarkers();

    void deleteMarkerById(Long id);

}
