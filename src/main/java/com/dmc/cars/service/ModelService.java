package com.dmc.cars.service;

import com.dmc.cars.domain.Model;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Model}.
 */
public interface ModelService {
    /**
     * Save a model.
     *
     * @param model the entity to save.
     * @return the persisted entity.
     */
    Model save(Model model);

    /**
     * Partially updates a model.
     *
     * @param model the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Model> partialUpdate(Model model);

    /**
     * Get all the models.
     *
     * @return the list of entities.
     */
    List<Model> findAll();

    /**
     * Get the "id" model.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Model> findOne(Long id);

    /**
     * Delete the "id" model.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
