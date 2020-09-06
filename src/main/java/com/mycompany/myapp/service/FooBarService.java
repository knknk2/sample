package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FooBar;
import com.mycompany.myapp.repository.FooBarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FooBar}.
 */
@Service
@Transactional
public class FooBarService {

    private final Logger log = LoggerFactory.getLogger(FooBarService.class);

    private final FooBarRepository fooBarRepository;

    public FooBarService(FooBarRepository fooBarRepository) {
        this.fooBarRepository = fooBarRepository;
    }

    /**
     * Save a fooBar.
     *
     * @param fooBar the entity to save.
     * @return the persisted entity.
     */
    public FooBar save(FooBar fooBar) {
        log.debug("Request to save FooBar : {}", fooBar);
        return fooBarRepository.save(fooBar);
    }

    /**
     * Get all the fooBars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FooBar> findAll(Pageable pageable) {
        log.debug("Request to get all FooBars");
        return fooBarRepository.findAll(pageable);
    }


    /**
     * Get one fooBar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FooBar> findOne(Long id) {
        log.debug("Request to get FooBar : {}", id);
        return fooBarRepository.findById(id);
    }

    /**
     * Delete the fooBar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FooBar : {}", id);
        fooBarRepository.deleteById(id);
    }
}
