package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Gearbox;
import com.dmc.cars.repository.GearboxRepository;
import com.dmc.cars.service.GearboxService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gearbox}.
 */
@Service
@Transactional
public class GearboxServiceImpl implements GearboxService {

    private final Logger log = LoggerFactory.getLogger(GearboxServiceImpl.class);

    private final GearboxRepository gearboxRepository;

    public GearboxServiceImpl(GearboxRepository gearboxRepository) {
        this.gearboxRepository = gearboxRepository;
    }

    @Override
    public Gearbox save(Gearbox gearbox) {
        log.debug("Request to save Gearbox : {}", gearbox);
        return gearboxRepository.save(gearbox);
    }

    @Override
    public Optional<Gearbox> partialUpdate(Gearbox gearbox) {
        log.debug("Request to partially update Gearbox : {}", gearbox);

        return gearboxRepository
            .findById(gearbox.getId())
            .map(existingGearbox -> {
                if (gearbox.getName() != null) {
                    existingGearbox.setName(gearbox.getName());
                }
                if (gearbox.getDescription() != null) {
                    existingGearbox.setDescription(gearbox.getDescription());
                }

                return existingGearbox;
            })
            .map(gearboxRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Gearbox> findAll() {
        log.debug("Request to get all Gearboxes");
        return gearboxRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Gearbox> findOne(Long id) {
        log.debug("Request to get Gearbox : {}", id);
        return gearboxRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gearbox : {}", id);
        gearboxRepository.deleteById(id);
    }
}
