package com.dmc.cars.service;

import com.dmc.cars.domain.City;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link City}.
 */
public interface CityService {
    /**
     * Save a city.
     *
     * @param city the entity to save.
     * @return the persisted entity.
     */
    City save(City city);

    /**
     * Partially updates a city.
     *
     * @param city the entity to update partially.
     * @return the persisted entity.
     */
    Optional<City> partialUpdate(City city);

    /**
     * Get all the cities.
     *
     * @return the list of entities.
     */
    List<City> findAll();

    /**
     * Get the "id" city.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<City> findOne(Long id);

    /**
     * Delete the "id" city.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
