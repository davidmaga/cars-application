package com.dmc.cars.web.rest;

import com.dmc.cars.domain.Gearbox;
import com.dmc.cars.repository.GearboxRepository;
import com.dmc.cars.service.GearboxService;
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
 * REST controller for managing {@link com.dmc.cars.domain.Gearbox}.
 */
@RestController
@RequestMapping("/api")
public class GearboxResource {

    private final Logger log = LoggerFactory.getLogger(GearboxResource.class);

    private static final String ENTITY_NAME = "gearbox";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GearboxService gearboxService;

    private final GearboxRepository gearboxRepository;

    public GearboxResource(GearboxService gearboxService, GearboxRepository gearboxRepository) {
        this.gearboxService = gearboxService;
        this.gearboxRepository = gearboxRepository;
    }

    /**
     * {@code POST  /gearboxes} : Create a new gearbox.
     *
     * @param gearbox the gearbox to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gearbox, or with status {@code 400 (Bad Request)} if the gearbox has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gearboxes")
    public ResponseEntity<Gearbox> createGearbox(@RequestBody Gearbox gearbox) throws URISyntaxException {
        log.debug("REST request to save Gearbox : {}", gearbox);
        if (gearbox.getId() != null) {
            throw new BadRequestAlertException("A new gearbox cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gearbox result = gearboxService.save(gearbox);
        return ResponseEntity
            .created(new URI("/api/gearboxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gearboxes/:id} : Updates an existing gearbox.
     *
     * @param id the id of the gearbox to save.
     * @param gearbox the gearbox to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gearbox,
     * or with status {@code 400 (Bad Request)} if the gearbox is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gearbox couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gearboxes/{id}")
    public ResponseEntity<Gearbox> updateGearbox(@PathVariable(value = "id", required = false) final Long id, @RequestBody Gearbox gearbox)
        throws URISyntaxException {
        log.debug("REST request to update Gearbox : {}, {}", id, gearbox);
        if (gearbox.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gearbox.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gearboxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Gearbox result = gearboxService.save(gearbox);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gearbox.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gearboxes/:id} : Partial updates given fields of an existing gearbox, field will ignore if it is null
     *
     * @param id the id of the gearbox to save.
     * @param gearbox the gearbox to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gearbox,
     * or with status {@code 400 (Bad Request)} if the gearbox is not valid,
     * or with status {@code 404 (Not Found)} if the gearbox is not found,
     * or with status {@code 500 (Internal Server Error)} if the gearbox couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gearboxes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Gearbox> partialUpdateGearbox(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Gearbox gearbox
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gearbox partially : {}, {}", id, gearbox);
        if (gearbox.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gearbox.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gearboxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gearbox> result = gearboxService.partialUpdate(gearbox);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gearbox.getId().toString())
        );
    }

    /**
     * {@code GET  /gearboxes} : get all the gearboxes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gearboxes in body.
     */
    @GetMapping("/gearboxes")
    public List<Gearbox> getAllGearboxes() {
        log.debug("REST request to get all Gearboxes");
        return gearboxService.findAll();
    }

    /**
     * {@code GET  /gearboxes/:id} : get the "id" gearbox.
     *
     * @param id the id of the gearbox to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gearbox, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gearboxes/{id}")
    public ResponseEntity<Gearbox> getGearbox(@PathVariable Long id) {
        log.debug("REST request to get Gearbox : {}", id);
        Optional<Gearbox> gearbox = gearboxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gearbox);
    }

    /**
     * {@code DELETE  /gearboxes/:id} : delete the "id" gearbox.
     *
     * @param id the id of the gearbox to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gearboxes/{id}")
    public ResponseEntity<Void> deleteGearbox(@PathVariable Long id) {
        log.debug("REST request to delete Gearbox : {}", id);
        gearboxService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
