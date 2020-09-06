package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.SampleApp;
import com.mycompany.myapp.domain.FooBar;
import com.mycompany.myapp.repository.FooBarRepository;
import com.mycompany.myapp.service.FooBarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FooBarResource} REST controller.
 */
@SpringBootTest(classes = SampleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class FooBarResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    @Autowired
    private FooBarRepository fooBarRepository;

    @Autowired
    private FooBarService fooBarService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFooBarMockMvc;

    private FooBar fooBar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FooBar createEntity(EntityManager em) {
        FooBar fooBar = new FooBar()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return fooBar;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FooBar createUpdatedEntity(EntityManager em) {
        FooBar fooBar = new FooBar()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        return fooBar;
    }

    @BeforeEach
    public void initTest() {
        fooBar = createEntity(em);
    }

    @Test
    @Transactional
    public void createFooBar() throws Exception {
        int databaseSizeBeforeCreate = fooBarRepository.findAll().size();
        // Create the FooBar
        restFooBarMockMvc.perform(post("/api/foo-bars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fooBar)))
            .andExpect(status().isCreated());

        // Validate the FooBar in the database
        List<FooBar> fooBarList = fooBarRepository.findAll();
        assertThat(fooBarList).hasSize(databaseSizeBeforeCreate + 1);
        FooBar testFooBar = fooBarList.get(fooBarList.size() - 1);
        assertThat(testFooBar.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testFooBar.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    public void createFooBarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fooBarRepository.findAll().size();

        // Create the FooBar with an existing ID
        fooBar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFooBarMockMvc.perform(post("/api/foo-bars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fooBar)))
            .andExpect(status().isBadRequest());

        // Validate the FooBar in the database
        List<FooBar> fooBarList = fooBarRepository.findAll();
        assertThat(fooBarList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFooBars() throws Exception {
        // Initialize the database
        fooBarRepository.saveAndFlush(fooBar);

        // Get all the fooBarList
        restFooBarMockMvc.perform(get("/api/foo-bars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fooBar.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }
    
    @Test
    @Transactional
    public void getFooBar() throws Exception {
        // Initialize the database
        fooBarRepository.saveAndFlush(fooBar);

        // Get the fooBar
        restFooBarMockMvc.perform(get("/api/foo-bars/{id}", fooBar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fooBar.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingFooBar() throws Exception {
        // Get the fooBar
        restFooBarMockMvc.perform(get("/api/foo-bars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFooBar() throws Exception {
        // Initialize the database
        fooBarService.save(fooBar);

        int databaseSizeBeforeUpdate = fooBarRepository.findAll().size();

        // Update the fooBar
        FooBar updatedFooBar = fooBarRepository.findById(fooBar.getId()).get();
        // Disconnect from session so that the updates on updatedFooBar are not directly saved in db
        em.detach(updatedFooBar);
        updatedFooBar
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);

        restFooBarMockMvc.perform(put("/api/foo-bars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFooBar)))
            .andExpect(status().isOk());

        // Validate the FooBar in the database
        List<FooBar> fooBarList = fooBarRepository.findAll();
        assertThat(fooBarList).hasSize(databaseSizeBeforeUpdate);
        FooBar testFooBar = fooBarList.get(fooBarList.size() - 1);
        assertThat(testFooBar.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testFooBar.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingFooBar() throws Exception {
        int databaseSizeBeforeUpdate = fooBarRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFooBarMockMvc.perform(put("/api/foo-bars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fooBar)))
            .andExpect(status().isBadRequest());

        // Validate the FooBar in the database
        List<FooBar> fooBarList = fooBarRepository.findAll();
        assertThat(fooBarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFooBar() throws Exception {
        // Initialize the database
        fooBarService.save(fooBar);

        int databaseSizeBeforeDelete = fooBarRepository.findAll().size();

        // Delete the fooBar
        restFooBarMockMvc.perform(delete("/api/foo-bars/{id}", fooBar.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FooBar> fooBarList = fooBarRepository.findAll();
        assertThat(fooBarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
