package com.dmc.cars.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dmc.cars.IntegrationTest;
import com.dmc.cars.domain.Photo;
import com.dmc.cars.repository.PhotoRepository;
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
 * Integration tests for the {@link PhotoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhotoResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MAIN = false;
    private static final Boolean UPDATED_MAIN = true;

    private static final String ENTITY_API_URL = "/api/photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhotoMockMvc;

    private Photo photo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createEntity(EntityManager em) {
        Photo photo = new Photo().path(DEFAULT_PATH).main(DEFAULT_MAIN);
        return photo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Photo createUpdatedEntity(EntityManager em) {
        Photo photo = new Photo().path(UPDATED_PATH).main(UPDATED_MAIN);
        return photo;
    }

    @BeforeEach
    public void initTest() {
        photo = createEntity(em);
    }

    @Test
    @Transactional
    void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();
        // Create the Photo
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate + 1);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testPhoto.getMain()).isEqualTo(DEFAULT_MAIN);
    }

    @Test
    @Transactional
    void createPhotoWithExistingId() throws Exception {
        // Create the Photo with an existing ID
        photo.setId(1L);

        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhotoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get all the photoList
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].main").value(hasItem(DEFAULT_MAIN.booleanValue())));
    }

    @Test
    @Transactional
    void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        // Get the photo
        restPhotoMockMvc
            .perform(get(ENTITY_API_URL_ID, photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(photo.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.main").value(DEFAULT_MAIN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        Photo updatedPhoto = photoRepository.findById(photo.getId()).get();
        // Disconnect from session so that the updates on updatedPhoto are not directly saved in db
        em.detach(updatedPhoto);
        updatedPhoto.path(UPDATED_PATH).main(UPDATED_MAIN);

        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhoto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhoto))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testPhoto.getMain()).isEqualTo(UPDATED_MAIN);
    }

    @Test
    @Transactional
    void putNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, photo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(photo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto.path(UPDATED_PATH);

        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoto))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testPhoto.getMain()).isEqualTo(DEFAULT_MAIN);
    }

    @Test
    @Transactional
    void fullUpdatePhotoWithPatch() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo using partial update
        Photo partialUpdatedPhoto = new Photo();
        partialUpdatedPhoto.setId(photo.getId());

        partialUpdatedPhoto.path(UPDATED_PATH).main(UPDATED_MAIN);

        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhoto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhoto))
            )
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photoList.get(photoList.size() - 1);
        assertThat(testPhoto.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testPhoto.getMain()).isEqualTo(UPDATED_MAIN);
    }

    @Test
    @Transactional
    void patchNonExistingPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, photo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(photo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhoto() throws Exception {
        int databaseSizeBeforeUpdate = photoRepository.findAll().size();
        photo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhotoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Photo in the database
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhoto() throws Exception {
        // Initialize the database
        photoRepository.saveAndFlush(photo);

        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Delete the photo
        restPhotoMockMvc
            .perform(delete(ENTITY_API_URL_ID, photo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Photo> photoList = photoRepository.findAll();
        assertThat(photoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
