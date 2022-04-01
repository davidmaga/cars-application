package com.dmc.cars.service;

import com.dmc.cars.domain.Dealer;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Dealer}.
 */
public interface DealerService {
    /**
     * Save a dealer.
     *
     * @param dealer the entity to save.
     * @return the persisted entity.
     */
    Dealer save(Dealer dealer);

    /**
     * Partially updates a dealer.
     *
     * @param dealer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dealer> partialUpdate(Dealer dealer);

    /**
     * Get all the dealers.
     *
     * @return the list of entities.
     */
    List<Dealer> findAll();

    /**
     * Get the "id" dealer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dealer> findOne(Long id);

    /**
     * Delete the "id" dealer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
