package com.dmc.cars.service;

import com.dmc.cars.domain.Brand;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Brand}.
 */
public interface BrandService {
    /**
     * Save a brand.
     *
     * @param brand the entity to save.
     * @return the persisted entity.
     */
    Brand save(Brand brand);

    /**
     * Partially updates a brand.
     *
     * @param brand the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Brand> partialUpdate(Brand brand);

    /**
     * Get all the brands.
     *
     * @return the list of entities.
     */
    List<Brand> findAll();

    /**
     * Get the "id" brand.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Brand> findOne(Long id);

    /**
     * Delete the "id" brand.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
