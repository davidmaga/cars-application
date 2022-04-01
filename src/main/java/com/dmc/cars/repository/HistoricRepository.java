package com.dmc.cars.repository;

import com.dmc.cars.domain.Historic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Historic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoricRepository extends JpaRepository<Historic, Long> {}
