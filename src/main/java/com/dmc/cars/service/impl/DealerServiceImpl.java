package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Dealer;
import com.dmc.cars.repository.DealerRepository;
import com.dmc.cars.service.DealerService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dealer}.
 */
@Service
@Transactional
public class DealerServiceImpl implements DealerService {

    private final Logger log = LoggerFactory.getLogger(DealerServiceImpl.class);

    private final DealerRepository dealerRepository;

    public DealerServiceImpl(DealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    @Override
    public Dealer save(Dealer dealer) {
        log.debug("Request to save Dealer : {}", dealer);
        return dealerRepository.save(dealer);
    }

    @Override
    public Optional<Dealer> partialUpdate(Dealer dealer) {
        log.debug("Request to partially update Dealer : {}", dealer);

        return dealerRepository
            .findById(dealer.getId())
            .map(existingDealer -> {
                if (dealer.getName() != null) {
                    existingDealer.setName(dealer.getName());
                }
                if (dealer.getDescription() != null) {
                    existingDealer.setDescription(dealer.getDescription());
                }
                if (dealer.getPhotoPath() != null) {
                    existingDealer.setPhotoPath(dealer.getPhotoPath());
                }
                if (dealer.getAddress() != null) {
                    existingDealer.setAddress(dealer.getAddress());
                }

                return existingDealer;
            })
            .map(dealerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Dealer> findAll() {
        log.debug("Request to get all Dealers");
        return dealerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dealer> findOne(Long id) {
        log.debug("Request to get Dealer : {}", id);
        return dealerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dealer : {}", id);
        dealerRepository.deleteById(id);
    }
}
