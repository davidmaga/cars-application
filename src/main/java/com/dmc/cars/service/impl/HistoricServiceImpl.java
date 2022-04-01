package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Historic;
import com.dmc.cars.repository.HistoricRepository;
import com.dmc.cars.service.HistoricService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Historic}.
 */
@Service
@Transactional
public class HistoricServiceImpl implements HistoricService {

    private final Logger log = LoggerFactory.getLogger(HistoricServiceImpl.class);

    private final HistoricRepository historicRepository;

    public HistoricServiceImpl(HistoricRepository historicRepository) {
        this.historicRepository = historicRepository;
    }

    @Override
    public Historic save(Historic historic) {
        log.debug("Request to save Historic : {}", historic);
        return historicRepository.save(historic);
    }

    @Override
    public Optional<Historic> partialUpdate(Historic historic) {
        log.debug("Request to partially update Historic : {}", historic);

        return historicRepository
            .findById(historic.getId())
            .map(existingHistoric -> {
                if (historic.getKms() != null) {
                    existingHistoric.setKms(historic.getKms());
                }
                if (historic.getPrice() != null) {
                    existingHistoric.setPrice(historic.getPrice());
                }

                return existingHistoric;
            })
            .map(historicRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Historic> findAll() {
        log.debug("Request to get all Historics");
        return historicRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Historic> findOne(Long id) {
        log.debug("Request to get Historic : {}", id);
        return historicRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Historic : {}", id);
        historicRepository.deleteById(id);
    }
}
