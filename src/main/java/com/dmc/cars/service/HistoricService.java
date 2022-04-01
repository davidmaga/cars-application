package com.dmc.cars.service;

import com.dmc.cars.domain.Historic;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Historic}.
 */
public interface HistoricService {
    /**
     * Save a historic.
     *
     * @param historic the entity to save.
     * @return the persisted entity.
     */
    Historic save(Historic historic);

    /**
     * Partially updates a historic.
     *
     * @param historic the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Historic> partialUpdate(Historic historic);

    /**
     * Get all the historics.
     *
     * @return the list of entities.
     */
    List<Historic> findAll();

    /**
     * Get the "id" historic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Historic> findOne(Long id);

    /**
     * Delete the "id" historic.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
