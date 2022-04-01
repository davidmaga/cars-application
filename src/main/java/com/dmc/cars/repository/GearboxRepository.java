package com.dmc.cars.repository;

import com.dmc.cars.domain.Gearbox;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Gearbox entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GearboxRepository extends JpaRepository<Gearbox, Long> {}
