package com.drago.spring.demo.services;

import com.drago.spring.demo.domain.Image;
import com.drago.spring.demo.domain.Marker;
import com.drago.spring.demo.domain.User;
import com.drago.spring.demo.exception.NoSuchMarkerException;
import com.drago.spring.demo.repositories.MarkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MarkerServiceImpl implements MarkerService {


    @Autowired
    private MarkerRepository markerRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Override
    public Marker save(Marker marker, MultipartFile[] files) {

        storageService.setUserDir(Paths.get(userService.getAuthenticatedUser().getLastName()));

        User user = userService.getAuthenticatedUser();

        marker.setUser(user);


        List<MultipartFile> listOfImages = imageService.filterAndGetCollection(files);


        uploadImages(marker, listOfImages);

        if (marker.getId() != null) {
            log.debug("it is update resolve orphans!");
            log.debug("-------------paths of new marker images-------------");
            marker.getImages().forEach(image -> System.out.println(Paths.get(image.getImagePath()).getFileName()));
            log.debug("-----------------paths of old marker images---------");
            markerRepository.getOne(marker.getId()).getImages().forEach(image -> System.out.println(Paths.get(image.getImagePath()).getFileName()));
            

            //markerRepository.getOne(marker.getId()).getImages().forEach(storageService.store();
        }

        return markerRepository.save(marker);
    }

    private void uploadImages(Marker marker, List<MultipartFile> listOfFiles) {
        listOfFiles.forEach(multipartFile -> {
            Path path = imageService.uploadImage(multipartFile);
            marker.addImage(new Image(multipartFile.getOriginalFilename(), path.toString()));
        });
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
