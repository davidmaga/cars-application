package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Photo;
import com.dmc.cars.repository.PhotoRepository;
import com.dmc.cars.service.PhotoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Photo}.
 */
@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo save(Photo photo) {
        log.debug("Request to save Photo : {}", photo);
        return photoRepository.save(photo);
    }

    @Override
    public Optional<Photo> partialUpdate(Photo photo) {
        log.debug("Request to partially update Photo : {}", photo);

        return photoRepository
            .findById(photo.getId())
            .map(existingPhoto -> {
                if (photo.getPath() != null) {
                    existingPhoto.setPath(photo.getPath());
                }
                if (photo.getMain() != null) {
                    existingPhoto.setMain(photo.getMain());
                }

                return existingPhoto;
            })
            .map(photoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Photo> findAll() {
        log.debug("Request to get all Photos");
        return photoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Photo> findOne(Long id) {
        log.debug("Request to get Photo : {}", id);
        return photoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.deleteById(id);
    }
}
