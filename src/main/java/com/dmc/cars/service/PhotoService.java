package com.dmc.cars.service;

import com.dmc.cars.domain.Photo;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Photo}.
 */
public interface PhotoService {
    /**
     * Save a photo.
     *
     * @param photo the entity to save.
     * @return the persisted entity.
     */
    Photo save(Photo photo);

    /**
     * Partially updates a photo.
     *
     * @param photo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Photo> partialUpdate(Photo photo);

    /**
     * Get all the photos.
     *
     * @return the list of entities.
     */
    List<Photo> findAll();

    /**
     * Get the "id" photo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Photo> findOne(Long id);

    /**
     * Delete the "id" photo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
