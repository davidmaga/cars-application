package com.dmc.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dmc.cars.IntegrationTest;
import com.dmc.cars.domain.Fuel;
import com.dmc.cars.repository.FuelRepository;
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
 * Integration tests for the {@link FuelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FuelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fuels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FuelRepository fuelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFuelMockMvc;

    private Fuel fuel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fuel createEntity(EntityManager em) {
        Fuel fuel = new Fuel().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return fuel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fuel createUpdatedEntity(EntityManager em) {
        Fuel fuel = new Fuel().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return fuel;
    }

    @BeforeEach
    public void initTest() {
        fuel = createEntity(em);
    }

    @Test
    @Transactional
    void createFuel() throws Exception {
        int databaseSizeBeforeCreate = fuelRepository.findAll().size();
        // Create the Fuel
        restFuelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isCreated());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeCreate + 1);
        Fuel testFuel = fuelList.get(fuelList.size() - 1);
        assertThat(testFuel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFuel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFuelWithExistingId() throws Exception {
        // Create the Fuel with an existing ID
        fuel.setId(1L);

        int databaseSizeBeforeCreate = fuelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFuels() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        // Get all the fuelList
        restFuelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fuel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFuel() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        // Get the fuel
        restFuelMockMvc
            .perform(get(ENTITY_API_URL_ID, fuel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fuel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingFuel() throws Exception {
        // Get the fuel
        restFuelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFuel() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();

        // Update the fuel
        Fuel updatedFuel = fuelRepository.findById(fuel.getId()).get();
        // Disconnect from session so that the updates on updatedFuel are not directly saved in db
        em.detach(updatedFuel);
        updatedFuel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFuelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFuel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFuel))
            )
            .andExpect(status().isOk());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
        Fuel testFuel = fuelList.get(fuelList.size() - 1);
        assertThat(testFuel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFuel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFuel() throws Exception {
        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();
        fuel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fuel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fuel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFuel() throws Exception {
        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();
        fuel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fuel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFuel() throws Exception {
        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();
        fuel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFuelWithPatch() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();

        // Update the fuel using partial update
        Fuel partialUpdatedFuel = new Fuel();
        partialUpdatedFuel.setId(fuel.getId());

        restFuelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuel))
            )
            .andExpect(status().isOk());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
        Fuel testFuel = fuelList.get(fuelList.size() - 1);
        assertThat(testFuel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFuel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFuelWithPatch() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();

        // Update the fuel using partial update
        Fuel partialUpdatedFuel = new Fuel();
        partialUpdatedFuel.setId(fuel.getId());

        partialUpdatedFuel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restFuelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFuel))
            )
            .andExpect(status().isOk());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
        Fuel testFuel = fuelList.get(fuelList.size() - 1);
        assertThat(testFuel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFuel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFuel() throws Exception {
        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();
        fuel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fuel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fuel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFuel() throws Exception {
        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();
        fuel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fuel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFuel() throws Exception {
        int databaseSizeBeforeUpdate = fuelRepository.findAll().size();
        fuel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fuel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fuel in the database
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFuel() throws Exception {
        // Initialize the database
        fuelRepository.saveAndFlush(fuel);

        int databaseSizeBeforeDelete = fuelRepository.findAll().size();

        // Delete the fuel
        restFuelMockMvc
            .perform(delete(ENTITY_API_URL_ID, fuel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fuel> fuelList = fuelRepository.findAll();
        assertThat(fuelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
