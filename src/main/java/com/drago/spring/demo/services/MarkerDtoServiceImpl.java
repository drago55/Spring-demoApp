package com.drago.spring.demo.services;

import com.drago.spring.demo.data_transfer_objects.MarkerDto;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.repositories.MarkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MarkerDtoServiceImpl implements MarkerDtoService {

    @Autowired
    private MarkerRepository markerRepository;


    @Override
    public Set<MarkerDto> getAllMarkers() {

        return markerRepository.findAll().stream().map((marker -> {

            final MarkerDto markerDto = new MarkerDto();
            setMarkerMetaData(markerDto, marker);


            setMarkerLocation(markerDto, marker);

            setMarkerCoordinates(markerDto, marker);


            setMarkerImages(markerDto, marker);

            return markerDto;
        })).collect(Collectors.toSet());


    }

    private void setMarkerImages(MarkerDto markerDto, Marker marker) {
        markerDto.setImages(marker.getImages().stream().map((image -> image.getImage())).collect(Collectors.toSet()));
    }

    private void setMarkerLocation(MarkerDto markerDto, Marker marker) {
        markerDto.setAddress(marker.getLocation().getStreetAddress());
        markerDto.setCity(marker.getLocation().getCity());
        markerDto.setState(marker.getLocation().getStateProvince());
        markerDto.setPostalCode(marker.getLocation().getPostalCode());
    }

    private void setMarkerCoordinates(MarkerDto markerDto, Marker marker) {
        markerDto.setLat(marker.getLatLon().getLat());
        markerDto.setLon(marker.getLatLon().getLon());
    }

    private void setMarkerMetaData(MarkerDto markerDto, Marker marker) {
        markerDto.setMarkerName(marker.getName());
        markerDto.setDescription(marker.getDescription());
        markerDto.setCreatedByUser(marker.getUser().getFirstName());
        markerDto.setMarkerType(marker.getMarkerType());
    }

    @Override
    public Set<Marker> getMarkersOfUserId(Long userId) {
        return null;
    }
}
