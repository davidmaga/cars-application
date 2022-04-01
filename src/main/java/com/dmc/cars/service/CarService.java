package com.dmc.cars.service;

import com.dmc.cars.domain.Car;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Car}.
 */
public interface CarService {
    /**
     * Save a car.
     *
     * @param car the entity to save.
     * @return the persisted entity.
     */
    Car save(Car car);

    /**
     * Partially updates a car.
     *
     * @param car the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Car> partialUpdate(Car car);

    /**
     * Get all the cars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Car> findAll(Pageable pageable);

    /**
     * Get the "id" car.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Car> findOne(Long id);

    /**
     * Delete the "id" car.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
