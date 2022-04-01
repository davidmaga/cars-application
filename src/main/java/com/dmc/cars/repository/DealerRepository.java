package com.dmc.cars.repository;

import com.dmc.cars.domain.Dealer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dealer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {}
