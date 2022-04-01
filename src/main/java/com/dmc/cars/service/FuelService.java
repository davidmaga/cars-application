package com.dmc.cars.service;

import com.dmc.cars.domain.Fuel;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Fuel}.
 */
public interface FuelService {
    /**
     * Save a fuel.
     *
     * @param fuel the entity to save.
     * @return the persisted entity.
     */
    Fuel save(Fuel fuel);

    /**
     * Partially updates a fuel.
     *
     * @param fuel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Fuel> partialUpdate(Fuel fuel);

    /**
     * Get all the fuels.
     *
     * @return the list of entities.
     */
    List<Fuel> findAll();

    /**
     * Get the "id" fuel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Fuel> findOne(Long id);

    /**
     * Delete the "id" fuel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
