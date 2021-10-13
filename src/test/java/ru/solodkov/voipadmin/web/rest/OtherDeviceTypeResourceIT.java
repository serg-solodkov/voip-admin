package ru.solodkov.voipadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import ru.solodkov.voipadmin.IntegrationTest;
import ru.solodkov.voipadmin.domain.OtherDeviceType;
import ru.solodkov.voipadmin.repository.OtherDeviceTypeRepository;
import ru.solodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import ru.solodkov.voipadmin.service.mapper.OtherDeviceTypeMapper;

/**
 * Integration tests for the {@link OtherDeviceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OtherDeviceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/other-device-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OtherDeviceTypeRepository otherDeviceTypeRepository;

    @Autowired
    private OtherDeviceTypeMapper otherDeviceTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOtherDeviceTypeMockMvc;

    private OtherDeviceType otherDeviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtherDeviceType createEntity(EntityManager em) {
        OtherDeviceType otherDeviceType = new OtherDeviceType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return otherDeviceType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtherDeviceType createUpdatedEntity(EntityManager em) {
        OtherDeviceType otherDeviceType = new OtherDeviceType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return otherDeviceType;
    }

    @BeforeEach
    public void initTest() {
        otherDeviceType = createEntity(em);
    }

    @Test
    @Transactional
    void createOtherDeviceType() throws Exception {
        int databaseSizeBeforeCreate = otherDeviceTypeRepository.findAll().size();
        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);
        restOtherDeviceTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        OtherDeviceType testOtherDeviceType = otherDeviceTypeList.get(otherDeviceTypeList.size() - 1);
        assertThat(testOtherDeviceType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOtherDeviceType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createOtherDeviceTypeWithExistingId() throws Exception {
        // Create the OtherDeviceType with an existing ID
        otherDeviceType.setId(1L);
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        int databaseSizeBeforeCreate = otherDeviceTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOtherDeviceTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOtherDeviceTypes() throws Exception {
        // Initialize the database
        otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        // Get all the otherDeviceTypeList
        restOtherDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otherDeviceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOtherDeviceType() throws Exception {
        // Initialize the database
        otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        // Get the otherDeviceType
        restOtherDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, otherDeviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(otherDeviceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingOtherDeviceType() throws Exception {
        // Get the otherDeviceType
        restOtherDeviceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOtherDeviceType() throws Exception {
        // Initialize the database
        otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();

        // Update the otherDeviceType
        OtherDeviceType updatedOtherDeviceType = otherDeviceTypeRepository.findById(otherDeviceType.getId()).get();
        // Disconnect from session so that the updates on updatedOtherDeviceType are not directly saved in db
        em.detach(updatedOtherDeviceType);
        updatedOtherDeviceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(updatedOtherDeviceType);

        restOtherDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otherDeviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
        OtherDeviceType testOtherDeviceType = otherDeviceTypeList.get(otherDeviceTypeList.size() - 1);
        assertThat(testOtherDeviceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOtherDeviceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingOtherDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();
        otherDeviceType.setId(count.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otherDeviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOtherDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();
        otherDeviceType.setId(count.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOtherDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();
        otherDeviceType.setId(count.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOtherDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();

        // Update the otherDeviceType using partial update
        OtherDeviceType partialUpdatedOtherDeviceType = new OtherDeviceType();
        partialUpdatedOtherDeviceType.setId(otherDeviceType.getId());

        partialUpdatedOtherDeviceType.name(UPDATED_NAME);

        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtherDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOtherDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
        OtherDeviceType testOtherDeviceType = otherDeviceTypeList.get(otherDeviceTypeList.size() - 1);
        assertThat(testOtherDeviceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOtherDeviceType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateOtherDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();

        // Update the otherDeviceType using partial update
        OtherDeviceType partialUpdatedOtherDeviceType = new OtherDeviceType();
        partialUpdatedOtherDeviceType.setId(otherDeviceType.getId());

        partialUpdatedOtherDeviceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtherDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOtherDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
        OtherDeviceType testOtherDeviceType = otherDeviceTypeList.get(otherDeviceTypeList.size() - 1);
        assertThat(testOtherDeviceType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOtherDeviceType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingOtherDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();
        otherDeviceType.setId(count.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, otherDeviceTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOtherDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();
        otherDeviceType.setId(count.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOtherDeviceType() throws Exception {
        int databaseSizeBeforeUpdate = otherDeviceTypeRepository.findAll().size();
        otherDeviceType.setId(count.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtherDeviceType in the database
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOtherDeviceType() throws Exception {
        // Initialize the database
        otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        int databaseSizeBeforeDelete = otherDeviceTypeRepository.findAll().size();

        // Delete the otherDeviceType
        restOtherDeviceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, otherDeviceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OtherDeviceType> otherDeviceTypeList = otherDeviceTypeRepository.findAll();
        assertThat(otherDeviceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
