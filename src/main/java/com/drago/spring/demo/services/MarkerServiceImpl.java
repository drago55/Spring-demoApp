package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.repositories.MarkerRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarkerServiceImpl implements MarkerService {

    @Autowired
    private MarkerRepositry markerRepositry;


    @Override
    public Marker save(Marker marker) {

        return markerRepositry.save(marker);

    }

    @Override
    public Optional<Marker> findMarkerById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Marker> findMarkerByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public List<Marker> findAllMarkers() {
        return markerRepositry.findAll();
    }

    @Override
    public void deleteMarkerById(Long id) {
        markerRepositry.delete(id);
    }
}
