package com.dmc.cars.web.rest;

import com.dmc.cars.domain.Fuel;
import com.dmc.cars.repository.FuelRepository;
import com.dmc.cars.service.FuelService;
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
 * REST controller for managing {@link com.dmc.cars.domain.Fuel}.
 */
@RestController
@RequestMapping("/api")
public class FuelResource {

    private final Logger log = LoggerFactory.getLogger(FuelResource.class);

    private static final String ENTITY_NAME = "fuel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuelService fuelService;

    private final FuelRepository fuelRepository;

    public FuelResource(FuelService fuelService, FuelRepository fuelRepository) {
        this.fuelService = fuelService;
        this.fuelRepository = fuelRepository;
    }

    /**
     * {@code POST  /fuels} : Create a new fuel.
     *
     * @param fuel the fuel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fuel, or with status {@code 400 (Bad Request)} if the fuel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fuels")
    public ResponseEntity<Fuel> createFuel(@RequestBody Fuel fuel) throws URISyntaxException {
        log.debug("REST request to save Fuel : {}", fuel);
        if (fuel.getId() != null) {
            throw new BadRequestAlertException("A new fuel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fuel result = fuelService.save(fuel);
        return ResponseEntity
            .created(new URI("/api/fuels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fuels/:id} : Updates an existing fuel.
     *
     * @param id the id of the fuel to save.
     * @param fuel the fuel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fuel,
     * or with status {@code 400 (Bad Request)} if the fuel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fuel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fuels/{id}")
    public ResponseEntity<Fuel> updateFuel(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fuel fuel)
        throws URISyntaxException {
        log.debug("REST request to update Fuel : {}, {}", id, fuel);
        if (fuel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fuel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fuelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fuel result = fuelService.save(fuel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fuel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fuels/:id} : Partial updates given fields of an existing fuel, field will ignore if it is null
     *
     * @param id the id of the fuel to save.
     * @param fuel the fuel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fuel,
     * or with status {@code 400 (Bad Request)} if the fuel is not valid,
     * or with status {@code 404 (Not Found)} if the fuel is not found,
     * or with status {@code 500 (Internal Server Error)} if the fuel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fuels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fuel> partialUpdateFuel(@PathVariable(value = "id", required = false) final Long id, @RequestBody Fuel fuel)
        throws URISyntaxException {
        log.debug("REST request to partial update Fuel partially : {}, {}", id, fuel);
        if (fuel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fuel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fuelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fuel> result = fuelService.partialUpdate(fuel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fuel.getId().toString())
        );
    }

    /**
     * {@code GET  /fuels} : get all the fuels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fuels in body.
     */
    @GetMapping("/fuels")
    public List<Fuel> getAllFuels() {
        log.debug("REST request to get all Fuels");
        return fuelService.findAll();
    }

    /**
     * {@code GET  /fuels/:id} : get the "id" fuel.
     *
     * @param id the id of the fuel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fuel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fuels/{id}")
    public ResponseEntity<Fuel> getFuel(@PathVariable Long id) {
        log.debug("REST request to get Fuel : {}", id);
        Optional<Fuel> fuel = fuelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fuel);
    }

    /**
     * {@code DELETE  /fuels/:id} : delete the "id" fuel.
     *
     * @param id the id of the fuel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fuels/{id}")
    public ResponseEntity<Void> deleteFuel(@PathVariable Long id) {
        log.debug("REST request to delete Fuel : {}", id);
        fuelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
