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
import ru.solodkov.voipadmin.domain.OptionValue;
import ru.solodkov.voipadmin.repository.OptionValueRepository;
import ru.solodkov.voipadmin.service.dto.OptionValueDTO;
import ru.solodkov.voipadmin.service.mapper.OptionValueMapper;

/**
 * Integration tests for the {@link OptionValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/option-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OptionValueRepository optionValueRepository;

    @Autowired
    private OptionValueMapper optionValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionValueMockMvc;

    private OptionValue optionValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionValue createEntity(EntityManager em) {
        OptionValue optionValue = new OptionValue().value(DEFAULT_VALUE);
        return optionValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionValue createUpdatedEntity(EntityManager em) {
        OptionValue optionValue = new OptionValue().value(UPDATED_VALUE);
        return optionValue;
    }

    @BeforeEach
    public void initTest() {
        optionValue = createEntity(em);
    }

    @Test
    @Transactional
    void createOptionValue() throws Exception {
        int databaseSizeBeforeCreate = optionValueRepository.findAll().size();
        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);
        restOptionValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeCreate + 1);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createOptionValueWithExistingId() throws Exception {
        // Create the OptionValue with an existing ID
        optionValue.setId(1L);
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        int databaseSizeBeforeCreate = optionValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionValueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOptionValues() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList
        restOptionValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getOptionValue() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        // Get the optionValue
        restOptionValueMockMvc
            .perform(get(ENTITY_API_URL_ID, optionValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(optionValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingOptionValue() throws Exception {
        // Get the optionValue
        restOptionValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOptionValue() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();

        // Update the optionValue
        OptionValue updatedOptionValue = optionValueRepository.findById(optionValue.getId()).get();
        // Disconnect from session so that the updates on updatedOptionValue are not directly saved in db
        em.detach(updatedOptionValue);
        updatedOptionValue.value(UPDATED_VALUE);
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(updatedOptionValue);

        restOptionValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();
        optionValue.setId(count.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();
        optionValue.setId(count.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();
        optionValue.setId(count.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionValueWithPatch() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();

        // Update the optionValue using partial update
        OptionValue partialUpdatedOptionValue = new OptionValue();
        partialUpdatedOptionValue.setId(optionValue.getId());

        partialUpdatedOptionValue.value(UPDATED_VALUE);

        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOptionValue))
            )
            .andExpect(status().isOk());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateOptionValueWithPatch() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();

        // Update the optionValue using partial update
        OptionValue partialUpdatedOptionValue = new OptionValue();
        partialUpdatedOptionValue.setId(optionValue.getId());

        partialUpdatedOptionValue.value(UPDATED_VALUE);

        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOptionValue))
            )
            .andExpect(status().isOk());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
        OptionValue testOptionValue = optionValueList.get(optionValueList.size() - 1);
        assertThat(testOptionValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();
        optionValue.setId(count.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, optionValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();
        optionValue.setId(count.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOptionValue() throws Exception {
        int databaseSizeBeforeUpdate = optionValueRepository.findAll().size();
        optionValue.setId(count.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(optionValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionValue in the database
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOptionValue() throws Exception {
        // Initialize the database
        optionValueRepository.saveAndFlush(optionValue);

        int databaseSizeBeforeDelete = optionValueRepository.findAll().size();

        // Delete the optionValue
        restOptionValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, optionValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OptionValue> optionValueList = optionValueRepository.findAll();
        assertThat(optionValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
