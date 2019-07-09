package com.drago.spring.demo.services;

import com.drago.spring.demo.data_transfer_objects.MarkerDto;
import com.drago.spring.demo.domain.Marker;

import java.util.Set;

public interface MarkerDtoService {

    Set<MarkerDto> getAllMarkers();

    Set<Marker> getMarkersByUserId(Long userId);
}
