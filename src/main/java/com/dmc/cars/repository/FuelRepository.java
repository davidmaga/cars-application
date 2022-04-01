package com.dmc.cars.repository;

import com.dmc.cars.domain.Fuel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Fuel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuelRepository extends JpaRepository<Fuel, Long> {}
