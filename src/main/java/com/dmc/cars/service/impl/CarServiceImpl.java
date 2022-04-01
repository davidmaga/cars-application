package com.dmc.cars.service.impl;

import com.dmc.cars.domain.Car;
import com.dmc.cars.repository.CarRepository;
import com.dmc.cars.service.CarService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Car}.
 */
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car save(Car car) {
        log.debug("Request to save Car : {}", car);
        return carRepository.save(car);
    }

    @Override
    public Optional<Car> partialUpdate(Car car) {
        log.debug("Request to partially update Car : {}", car);

        return carRepository
            .findById(car.getId())
            .map(existingCar -> {
                if (car.getName() != null) {
                    existingCar.setName(car.getName());
                }
                if (car.getDescription() != null) {
                    existingCar.setDescription(car.getDescription());
                }
                if (car.getEngine() != null) {
                    existingCar.setEngine(car.getEngine());
                }
                if (car.getPower() != null) {
                    existingCar.setPower(car.getPower());
                }
                if (car.getKms() != null) {
                    existingCar.setKms(car.getKms());
                }
                if (car.getColor() != null) {
                    existingCar.setColor(car.getColor());
                }
                if (car.getDoors() != null) {
                    existingCar.setDoors(car.getDoors());
                }
                if (car.getSeats() != null) {
                    existingCar.setSeats(car.getSeats());
                }
                if (car.getBuildingDate() != null) {
                    existingCar.setBuildingDate(car.getBuildingDate());
                }
                if (car.getPrice() != null) {
                    existingCar.setPrice(car.getPrice());
                }
                if (car.getOffer() != null) {
                    existingCar.setOffer(car.getOffer());
                }

                return existingCar;
            })
            .map(carRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Car> findAll(Pageable pageable) {
        log.debug("Request to get all Cars");
        return carRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Car> findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }
}
