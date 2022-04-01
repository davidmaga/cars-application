package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Brand;
import com.dmc.cars.repository.BrandRepository;
import com.dmc.cars.service.BrandService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Brand}.
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand save(Brand brand) {
        log.debug("Request to save Brand : {}", brand);
        return brandRepository.save(brand);
    }

    @Override
    public Optional<Brand> partialUpdate(Brand brand) {
        log.debug("Request to partially update Brand : {}", brand);

        return brandRepository
            .findById(brand.getId())
            .map(existingBrand -> {
                if (brand.getName() != null) {
                    existingBrand.setName(brand.getName());
                }
                if (brand.getDescription() != null) {
                    existingBrand.setDescription(brand.getDescription());
                }
                if (brand.getPhotoPath() != null) {
                    existingBrand.setPhotoPath(brand.getPhotoPath());
                }

                return existingBrand;
            })
            .map(brandRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        log.debug("Request to get all Brands");
        return brandRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Brand> findOne(Long id) {
        log.debug("Request to get Brand : {}", id);
        return brandRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Brand : {}", id);
        brandRepository.deleteById(id);
    }
}
