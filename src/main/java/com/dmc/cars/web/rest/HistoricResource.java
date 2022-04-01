package com.dmc.cars.web.rest;

import com.dmc.cars.domain.Historic;
import com.dmc.cars.repository.HistoricRepository;
import com.dmc.cars.service.HistoricService;
import com.dmc.cars.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.dmc.cars.domain.Historic}.
 */
@RestController
@RequestMapping("/api")
public class HistoricResource {

    private final Logger log = LoggerFactory.getLogger(HistoricResource.class);

    private static final String ENTITY_NAME = "historic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoricService historicService;

    private final HistoricRepository historicRepository;

    public HistoricResource(HistoricService historicService, HistoricRepository historicRepository) {
        this.historicService = historicService;
        this.historicRepository = historicRepository;
    }

    /**
     * {@code POST  /historics} : Create a new historic.
     *
     * @param historic the historic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historic, or with status {@code 400 (Bad Request)} if the historic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/historics")
    public ResponseEntity<Historic> createHistoric(@RequestBody Historic historic) throws URISyntaxException {
        log.debug("REST request to save Historic : {}", historic);
        if (historic.getId() != null) {
            throw new BadRequestAlertException("A new historic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Historic result = historicService.save(historic);
        return ResponseEntity
            .created(new URI("/api/historics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /historics/:id} : Updates an existing historic.
     *
     * @param id the id of the historic to save.
     * @param historic the historic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historic,
     * or with status {@code 400 (Bad Request)} if the historic is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/historics/{id}")
    public ResponseEntity<Historic> updateHistoric(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Historic historic
    ) throws URISyntaxException {
        log.debug("REST request to update Historic : {}, {}", id, historic);
        if (historic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Historic result = historicService.save(historic);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historic.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /historics/:id} : Partial updates given fields of an existing historic, field will ignore if it is null
     *
     * @param id the id of the historic to save.
     * @param historic the historic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historic,
     * or with status {@code 400 (Bad Request)} if the historic is not valid,
     * or with status {@code 404 (Not Found)} if the historic is not found,
     * or with status {@code 500 (Internal Server Error)} if the historic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/historics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Historic> partialUpdateHistoric(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Historic historic
    ) throws URISyntaxException {
        log.debug("REST request to partial update Historic partially : {}, {}", id, historic);
        if (historic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Historic> result = historicService.partialUpdate(historic);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historic.getId().toString())
        );
    }

    /**
     * {@code GET  /historics} : get all the historics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historics in body.
     */
    @GetMapping("/historics")
    public List<Historic> getAllHistorics() {
        log.debug("REST request to get all Historics");
        return historicService.findAll();
    }

    /**
     * {@code GET  /historics/:id} : get the "id" historic.
     *
     * @param id the id of the historic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/historics/{id}")
    public ResponseEntity<Historic> getHistoric(@PathVariable Long id) {
        log.debug("REST request to get Historic : {}", id);
        Optional<Historic> historic = historicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historic);
    }

    /**
     * {@code DELETE  /historics/:id} : delete the "id" historic.
     *
     * @param id the id of the historic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/historics/{id}")
    public ResponseEntity<Void> deleteHistoric(@PathVariable Long id) {
        log.debug("REST request to delete Historic : {}", id);
        historicService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
