package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import com.drago.spring.demo.repositories.MarkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarkerServiceImpl implements MarkerService {


    @Autowired
    private MarkerRepository markerRepository;


    @Override
    public Marker save(Marker marker) {
        return markerRepository.save(marker);
    }

    @Override
    public Marker findMarkerById(Long id) {
        if (!markerRepository.exists(id)) {
            throw new NoSuchMarkerException("Marker don't exists!");
        }
        return markerRepository.findOne(id);
    }

    @Override
    public Optional<Marker> findMarkerByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public List<Marker> findAllMarkers() {


        return markerRepository.findAll();
    }

    @Override
    public void deleteMarkerById(Long id) {
        markerRepository.delete(id);
    }
}
