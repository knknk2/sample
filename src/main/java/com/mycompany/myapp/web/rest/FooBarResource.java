package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FooBar;
import com.mycompany.myapp.service.FooBarService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.FooBar}.
 */
@RestController
@RequestMapping("/api")
public class FooBarResource {

    private final Logger log = LoggerFactory.getLogger(FooBarResource.class);

    private static final String ENTITY_NAME = "fooBar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FooBarService fooBarService;

    public FooBarResource(FooBarService fooBarService) {
        this.fooBarService = fooBarService;
    }

    /**
     * {@code POST  /foo-bars} : Create a new fooBar.
     *
     * @param fooBar the fooBar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fooBar, or with status {@code 400 (Bad Request)} if the fooBar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/foo-bars")
    public ResponseEntity<FooBar> createFooBar(@RequestBody FooBar fooBar) throws URISyntaxException {
        log.debug("REST request to save FooBar : {}", fooBar);
        if (fooBar.getId() != null) {
            throw new BadRequestAlertException("A new fooBar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FooBar result = fooBarService.save(fooBar);
        return ResponseEntity.created(new URI("/api/foo-bars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /foo-bars} : Updates an existing fooBar.
     *
     * @param fooBar the fooBar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fooBar,
     * or with status {@code 400 (Bad Request)} if the fooBar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fooBar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/foo-bars")
    public ResponseEntity<FooBar> updateFooBar(@RequestBody FooBar fooBar) throws URISyntaxException {
        log.debug("REST request to update FooBar : {}", fooBar);
        if (fooBar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FooBar result = fooBarService.save(fooBar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fooBar.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /foo-bars} : get all the fooBars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fooBars in body.
     */
    @GetMapping("/foo-bars")
    public ResponseEntity<List<FooBar>> getAllFooBars(Pageable pageable) {
        log.debug("REST request to get a page of FooBars");
        Page<FooBar> page = fooBarService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /foo-bars/:id} : get the "id" fooBar.
     *
     * @param id the id of the fooBar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fooBar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/foo-bars/{id}")
    public ResponseEntity<FooBar> getFooBar(@PathVariable Long id) {
        log.debug("REST request to get FooBar : {}", id);
        Optional<FooBar> fooBar = fooBarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fooBar);
    }

    /**
     * {@code DELETE  /foo-bars/:id} : delete the "id" fooBar.
     *
     * @param id the id of the fooBar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/foo-bars/{id}")
    public ResponseEntity<Void> deleteFooBar(@PathVariable Long id) {
        log.debug("REST request to delete FooBar : {}", id);
        fooBarService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
