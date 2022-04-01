package com.dmc.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dmc.cars.IntegrationTest;
import com.dmc.cars.domain.Gearbox;
import com.dmc.cars.repository.GearboxRepository;
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
 * Integration tests for the {@link GearboxResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GearboxResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gearboxes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GearboxRepository gearboxRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGearboxMockMvc;

    private Gearbox gearbox;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gearbox createEntity(EntityManager em) {
        Gearbox gearbox = new Gearbox().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return gearbox;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gearbox createUpdatedEntity(EntityManager em) {
        Gearbox gearbox = new Gearbox().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return gearbox;
    }

    @BeforeEach
    public void initTest() {
        gearbox = createEntity(em);
    }

    @Test
    @Transactional
    void createGearbox() throws Exception {
        int databaseSizeBeforeCreate = gearboxRepository.findAll().size();
        // Create the Gearbox
        restGearboxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gearbox)))
            .andExpect(status().isCreated());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeCreate + 1);
        Gearbox testGearbox = gearboxList.get(gearboxList.size() - 1);
        assertThat(testGearbox.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGearbox.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createGearboxWithExistingId() throws Exception {
        // Create the Gearbox with an existing ID
        gearbox.setId(1L);

        int databaseSizeBeforeCreate = gearboxRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGearboxMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gearbox)))
            .andExpect(status().isBadRequest());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGearboxes() throws Exception {
        // Initialize the database
        gearboxRepository.saveAndFlush(gearbox);

        // Get all the gearboxList
        restGearboxMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gearbox.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getGearbox() throws Exception {
        // Initialize the database
        gearboxRepository.saveAndFlush(gearbox);

        // Get the gearbox
        restGearboxMockMvc
            .perform(get(ENTITY_API_URL_ID, gearbox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gearbox.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingGearbox() throws Exception {
        // Get the gearbox
        restGearboxMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGearbox() throws Exception {
        // Initialize the database
        gearboxRepository.saveAndFlush(gearbox);

        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();

        // Update the gearbox
        Gearbox updatedGearbox = gearboxRepository.findById(gearbox.getId()).get();
        // Disconnect from session so that the updates on updatedGearbox are not directly saved in db
        em.detach(updatedGearbox);
        updatedGearbox.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restGearboxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGearbox.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGearbox))
            )
            .andExpect(status().isOk());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
        Gearbox testGearbox = gearboxList.get(gearboxList.size() - 1);
        assertThat(testGearbox.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGearbox.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingGearbox() throws Exception {
        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();
        gearbox.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGearboxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gearbox.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gearbox))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGearbox() throws Exception {
        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();
        gearbox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGearboxMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gearbox))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGearbox() throws Exception {
        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();
        gearbox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGearboxMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gearbox)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGearboxWithPatch() throws Exception {
        // Initialize the database
        gearboxRepository.saveAndFlush(gearbox);

        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();

        // Update the gearbox using partial update
        Gearbox partialUpdatedGearbox = new Gearbox();
        partialUpdatedGearbox.setId(gearbox.getId());

        partialUpdatedGearbox.name(UPDATED_NAME);

        restGearboxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGearbox.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGearbox))
            )
            .andExpect(status().isOk());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
        Gearbox testGearbox = gearboxList.get(gearboxList.size() - 1);
        assertThat(testGearbox.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGearbox.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateGearboxWithPatch() throws Exception {
        // Initialize the database
        gearboxRepository.saveAndFlush(gearbox);

        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();

        // Update the gearbox using partial update
        Gearbox partialUpdatedGearbox = new Gearbox();
        partialUpdatedGearbox.setId(gearbox.getId());

        partialUpdatedGearbox.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restGearboxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGearbox.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGearbox))
            )
            .andExpect(status().isOk());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
        Gearbox testGearbox = gearboxList.get(gearboxList.size() - 1);
        assertThat(testGearbox.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGearbox.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingGearbox() throws Exception {
        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();
        gearbox.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGearboxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gearbox.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gearbox))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGearbox() throws Exception {
        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();
        gearbox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGearboxMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gearbox))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGearbox() throws Exception {
        int databaseSizeBeforeUpdate = gearboxRepository.findAll().size();
        gearbox.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGearboxMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gearbox)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gearbox in the database
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGearbox() throws Exception {
        // Initialize the database
        gearboxRepository.saveAndFlush(gearbox);

        int databaseSizeBeforeDelete = gearboxRepository.findAll().size();

        // Delete the gearbox
        restGearboxMockMvc
            .perform(delete(ENTITY_API_URL_ID, gearbox.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gearbox> gearboxList = gearboxRepository.findAll();
        assertThat(gearboxList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
