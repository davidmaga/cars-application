package com.dmc.cars.service;

import com.dmc.cars.domain.Gearbox;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Gearbox}.
 */
public interface GearboxService {
    /**
     * Save a gearbox.
     *
     * @param gearbox the entity to save.
     * @return the persisted entity.
     */
    Gearbox save(Gearbox gearbox);

    /**
     * Partially updates a gearbox.
     *
     * @param gearbox the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Gearbox> partialUpdate(Gearbox gearbox);

    /**
     * Get all the gearboxes.
     *
     * @return the list of entities.
     */
    List<Gearbox> findAll();

    /**
     * Get the "id" gearbox.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Gearbox> findOne(Long id);

    /**
     * Delete the "id" gearbox.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
