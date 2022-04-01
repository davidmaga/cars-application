package com.dmc.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dmc.cars.IntegrationTest;
import com.dmc.cars.domain.Car;
import com.dmc.cars.repository.CarRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ENGINE = "AAAAAAAAAA";
    private static final String UPDATED_ENGINE = "BBBBBBBBBB";

    private static final Integer DEFAULT_POWER = 1;
    private static final Integer UPDATED_POWER = 2;

    private static final Integer DEFAULT_KMS = 1;
    private static final Integer UPDATED_KMS = 2;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOORS = 1;
    private static final Integer UPDATED_DOORS = 2;

    private static final Integer DEFAULT_SEATS = 1;
    private static final Integer UPDATED_SEATS = 2;

    private static final Instant DEFAULT_BUILDING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BUILDING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Boolean DEFAULT_OFFER = false;
    private static final Boolean UPDATED_OFFER = true;

    private static final String ENTITY_API_URL = "/api/cars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarMockMvc;

    private Car car;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .engine(DEFAULT_ENGINE)
            .power(DEFAULT_POWER)
            .kms(DEFAULT_KMS)
            .color(DEFAULT_COLOR)
            .doors(DEFAULT_DOORS)
            .seats(DEFAULT_SEATS)
            .buildingDate(DEFAULT_BUILDING_DATE)
            .price(DEFAULT_PRICE)
            .offer(DEFAULT_OFFER);
        return car;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .engine(UPDATED_ENGINE)
            .power(UPDATED_POWER)
            .kms(UPDATED_KMS)
            .color(UPDATED_COLOR)
            .doors(UPDATED_DOORS)
            .seats(UPDATED_SEATS)
            .buildingDate(UPDATED_BUILDING_DATE)
            .price(UPDATED_PRICE)
            .offer(UPDATED_OFFER);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();
        // Create the Car
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCar.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCar.getEngine()).isEqualTo(DEFAULT_ENGINE);
        assertThat(testCar.getPower()).isEqualTo(DEFAULT_POWER);
        assertThat(testCar.getKms()).isEqualTo(DEFAULT_KMS);
        assertThat(testCar.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCar.getDoors()).isEqualTo(DEFAULT_DOORS);
        assertThat(testCar.getSeats()).isEqualTo(DEFAULT_SEATS);
        assertThat(testCar.getBuildingDate()).isEqualTo(DEFAULT_BUILDING_DATE);
        assertThat(testCar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCar.getOffer()).isEqualTo(DEFAULT_OFFER);
    }

    @Test
    @Transactional
    void createCarWithExistingId() throws Exception {
        // Create the Car with an existing ID
        car.setId(1L);

        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carRepository.findAll().size();
        // set the field null
        car.setName(null);

        // Create the Car, which fails.

        restCarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].engine").value(hasItem(DEFAULT_ENGINE)))
            .andExpect(jsonPath("$.[*].power").value(hasItem(DEFAULT_POWER)))
            .andExpect(jsonPath("$.[*].kms").value(hasItem(DEFAULT_KMS)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].doors").value(hasItem(DEFAULT_DOORS)))
            .andExpect(jsonPath("$.[*].seats").value(hasItem(DEFAULT_SEATS)))
            .andExpect(jsonPath("$.[*].buildingDate").value(hasItem(DEFAULT_BUILDING_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].offer").value(hasItem(DEFAULT_OFFER.booleanValue())));
    }

    @Test
    @Transactional
    void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc
            .perform(get(ENTITY_API_URL_ID, car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.engine").value(DEFAULT_ENGINE))
            .andExpect(jsonPath("$.power").value(DEFAULT_POWER))
            .andExpect(jsonPath("$.kms").value(DEFAULT_KMS))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.doors").value(DEFAULT_DOORS))
            .andExpect(jsonPath("$.seats").value(DEFAULT_SEATS))
            .andExpect(jsonPath("$.buildingDate").value(DEFAULT_BUILDING_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.offer").value(DEFAULT_OFFER.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .engine(UPDATED_ENGINE)
            .power(UPDATED_POWER)
            .kms(UPDATED_KMS)
            .color(UPDATED_COLOR)
            .doors(UPDATED_DOORS)
            .seats(UPDATED_SEATS)
            .buildingDate(UPDATED_BUILDING_DATE)
            .price(UPDATED_PRICE)
            .offer(UPDATED_OFFER);

        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCar.getEngine()).isEqualTo(UPDATED_ENGINE);
        assertThat(testCar.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testCar.getKms()).isEqualTo(UPDATED_KMS);
        assertThat(testCar.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCar.getDoors()).isEqualTo(UPDATED_DOORS);
        assertThat(testCar.getSeats()).isEqualTo(UPDATED_SEATS);
        assertThat(testCar.getBuildingDate()).isEqualTo(UPDATED_BUILDING_DATE);
        assertThat(testCar.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCar.getOffer()).isEqualTo(UPDATED_OFFER);
    }

    @Test
    @Transactional
    void putNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, car.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .engine(UPDATED_ENGINE)
            .power(UPDATED_POWER)
            .kms(UPDATED_KMS)
            .doors(UPDATED_DOORS)
            .buildingDate(UPDATED_BUILDING_DATE)
            .offer(UPDATED_OFFER);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCar.getEngine()).isEqualTo(UPDATED_ENGINE);
        assertThat(testCar.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testCar.getKms()).isEqualTo(UPDATED_KMS);
        assertThat(testCar.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCar.getDoors()).isEqualTo(UPDATED_DOORS);
        assertThat(testCar.getSeats()).isEqualTo(DEFAULT_SEATS);
        assertThat(testCar.getBuildingDate()).isEqualTo(UPDATED_BUILDING_DATE);
        assertThat(testCar.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCar.getOffer()).isEqualTo(UPDATED_OFFER);
    }

    @Test
    @Transactional
    void fullUpdateCarWithPatch() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car using partial update
        Car partialUpdatedCar = new Car();
        partialUpdatedCar.setId(car.getId());

        partialUpdatedCar
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .engine(UPDATED_ENGINE)
            .power(UPDATED_POWER)
            .kms(UPDATED_KMS)
            .color(UPDATED_COLOR)
            .doors(UPDATED_DOORS)
            .seats(UPDATED_SEATS)
            .buildingDate(UPDATED_BUILDING_DATE)
            .price(UPDATED_PRICE)
            .offer(UPDATED_OFFER);

        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCar))
            )
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCar.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCar.getEngine()).isEqualTo(UPDATED_ENGINE);
        assertThat(testCar.getPower()).isEqualTo(UPDATED_POWER);
        assertThat(testCar.getKms()).isEqualTo(UPDATED_KMS);
        assertThat(testCar.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCar.getDoors()).isEqualTo(UPDATED_DOORS);
        assertThat(testCar.getSeats()).isEqualTo(UPDATED_SEATS);
        assertThat(testCar.getBuildingDate()).isEqualTo(UPDATED_BUILDING_DATE);
        assertThat(testCar.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCar.getOffer()).isEqualTo(UPDATED_OFFER);
    }

    @Test
    @Transactional
    void patchNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, car.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(car))
            )
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();
        car.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc.perform(delete(ENTITY_API_URL_ID, car.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
