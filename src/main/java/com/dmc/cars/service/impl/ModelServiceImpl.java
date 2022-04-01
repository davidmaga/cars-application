package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Model;
import com.dmc.cars.repository.ModelRepository;
import com.dmc.cars.service.ModelService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Model}.
 */
@Service
@Transactional
public class ModelServiceImpl implements ModelService {

    private final Logger log = LoggerFactory.getLogger(ModelServiceImpl.class);

    private final ModelRepository modelRepository;

    public ModelServiceImpl(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @Override
    public Model save(Model model) {
        log.debug("Request to save Model : {}", model);
        return modelRepository.save(model);
    }

    @Override
    public Optional<Model> partialUpdate(Model model) {
        log.debug("Request to partially update Model : {}", model);

        return modelRepository
            .findById(model.getId())
            .map(existingModel -> {
                if (model.getName() != null) {
                    existingModel.setName(model.getName());
                }
                if (model.getDescription() != null) {
                    existingModel.setDescription(model.getDescription());
                }

                return existingModel;
            })
            .map(modelRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Model> findAll() {
        log.debug("Request to get all Models");
        return modelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Model> findOne(Long id) {
        log.debug("Request to get Model : {}", id);
        return modelRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Model : {}", id);
        modelRepository.deleteById(id);
    }
}
