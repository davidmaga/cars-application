package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Fuel;
import com.dmc.cars.repository.FuelRepository;
import com.dmc.cars.service.FuelService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fuel}.
 */
@Service
@Transactional
public class FuelServiceImpl implements FuelService {

    private final Logger log = LoggerFactory.getLogger(FuelServiceImpl.class);

    private final FuelRepository fuelRepository;

    public FuelServiceImpl(FuelRepository fuelRepository) {
        this.fuelRepository = fuelRepository;
    }

    @Override
    public Fuel save(Fuel fuel) {
        log.debug("Request to save Fuel : {}", fuel);
        return fuelRepository.save(fuel);
    }

    @Override
    public Optional<Fuel> partialUpdate(Fuel fuel) {
        log.debug("Request to partially update Fuel : {}", fuel);

        return fuelRepository
            .findById(fuel.getId())
            .map(existingFuel -> {
                if (fuel.getName() != null) {
                    existingFuel.setName(fuel.getName());
                }
                if (fuel.getDescription() != null) {
                    existingFuel.setDescription(fuel.getDescription());
                }

                return existingFuel;
            })
            .map(fuelRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fuel> findAll() {
        log.debug("Request to get all Fuels");
        return fuelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fuel> findOne(Long id) {
        log.debug("Request to get Fuel : {}", id);
        return fuelRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fuel : {}", id);
        fuelRepository.deleteById(id);
    }
}
