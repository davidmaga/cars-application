package com.dmc.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dmc.cars.IntegrationTest;
import com.dmc.cars.domain.Historic;
import com.dmc.cars.repository.HistoricRepository;
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
 * Integration tests for the {@link HistoricResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoricResourceIT {

    private static final Integer DEFAULT_KMS = 1;
    private static final Integer UPDATED_KMS = 2;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/historics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoricRepository historicRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoricMockMvc;

    private Historic historic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historic createEntity(EntityManager em) {
        Historic historic = new Historic().kms(DEFAULT_KMS).price(DEFAULT_PRICE);
        return historic;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historic createUpdatedEntity(EntityManager em) {
        Historic historic = new Historic().kms(UPDATED_KMS).price(UPDATED_PRICE);
        return historic;
    }

    @BeforeEach
    public void initTest() {
        historic = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoric() throws Exception {
        int databaseSizeBeforeCreate = historicRepository.findAll().size();
        // Create the Historic
        restHistoricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historic)))
            .andExpect(status().isCreated());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeCreate + 1);
        Historic testHistoric = historicList.get(historicList.size() - 1);
        assertThat(testHistoric.getKms()).isEqualTo(DEFAULT_KMS);
        assertThat(testHistoric.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createHistoricWithExistingId() throws Exception {
        // Create the Historic with an existing ID
        historic.setId(1L);

        int databaseSizeBeforeCreate = historicRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historic)))
            .andExpect(status().isBadRequest());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHistorics() throws Exception {
        // Initialize the database
        historicRepository.saveAndFlush(historic);

        // Get all the historicList
        restHistoricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historic.getId().intValue())))
            .andExpect(jsonPath("$.[*].kms").value(hasItem(DEFAULT_KMS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getHistoric() throws Exception {
        // Initialize the database
        historicRepository.saveAndFlush(historic);

        // Get the historic
        restHistoricMockMvc
            .perform(get(ENTITY_API_URL_ID, historic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historic.getId().intValue()))
            .andExpect(jsonPath("$.kms").value(DEFAULT_KMS))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingHistoric() throws Exception {
        // Get the historic
        restHistoricMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHistoric() throws Exception {
        // Initialize the database
        historicRepository.saveAndFlush(historic);

        int databaseSizeBeforeUpdate = historicRepository.findAll().size();

        // Update the historic
        Historic updatedHistoric = historicRepository.findById(historic.getId()).get();
        // Disconnect from session so that the updates on updatedHistoric are not directly saved in db
        em.detach(updatedHistoric);
        updatedHistoric.kms(UPDATED_KMS).price(UPDATED_PRICE);

        restHistoricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHistoric.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHistoric))
            )
            .andExpect(status().isOk());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
        Historic testHistoric = historicList.get(historicList.size() - 1);
        assertThat(testHistoric.getKms()).isEqualTo(UPDATED_KMS);
        assertThat(testHistoric.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingHistoric() throws Exception {
        int databaseSizeBeforeUpdate = historicRepository.findAll().size();
        historic.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoric() throws Exception {
        int databaseSizeBeforeUpdate = historicRepository.findAll().size();
        historic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoric() throws Exception {
        int databaseSizeBeforeUpdate = historicRepository.findAll().size();
        historic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(historic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoricWithPatch() throws Exception {
        // Initialize the database
        historicRepository.saveAndFlush(historic);

        int databaseSizeBeforeUpdate = historicRepository.findAll().size();

        // Update the historic using partial update
        Historic partialUpdatedHistoric = new Historic();
        partialUpdatedHistoric.setId(historic.getId());

        partialUpdatedHistoric.kms(UPDATED_KMS);

        restHistoricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoric.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoric))
            )
            .andExpect(status().isOk());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
        Historic testHistoric = historicList.get(historicList.size() - 1);
        assertThat(testHistoric.getKms()).isEqualTo(UPDATED_KMS);
        assertThat(testHistoric.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateHistoricWithPatch() throws Exception {
        // Initialize the database
        historicRepository.saveAndFlush(historic);

        int databaseSizeBeforeUpdate = historicRepository.findAll().size();

        // Update the historic using partial update
        Historic partialUpdatedHistoric = new Historic();
        partialUpdatedHistoric.setId(historic.getId());

        partialUpdatedHistoric.kms(UPDATED_KMS).price(UPDATED_PRICE);

        restHistoricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoric.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoric))
            )
            .andExpect(status().isOk());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
        Historic testHistoric = historicList.get(historicList.size() - 1);
        assertThat(testHistoric.getKms()).isEqualTo(UPDATED_KMS);
        assertThat(testHistoric.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingHistoric() throws Exception {
        int databaseSizeBeforeUpdate = historicRepository.findAll().size();
        historic.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historic.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoric() throws Exception {
        int databaseSizeBeforeUpdate = historicRepository.findAll().size();
        historic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historic))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoric() throws Exception {
        int databaseSizeBeforeUpdate = historicRepository.findAll().size();
        historic.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(historic)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historic in the database
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoric() throws Exception {
        // Initialize the database
        historicRepository.saveAndFlush(historic);

        int databaseSizeBeforeDelete = historicRepository.findAll().size();

        // Delete the historic
        restHistoricMockMvc
            .perform(delete(ENTITY_API_URL_ID, historic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Historic> historicList = historicRepository.findAll();
        assertThat(historicList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
