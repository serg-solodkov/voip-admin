package ru.solodkov.voipadmin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.IntegrationTest;
import ru.solodkov.voipadmin.domain.Option;
import ru.solodkov.voipadmin.domain.enumeration.OptionValueType;
import ru.solodkov.voipadmin.repository.OptionRepository;
import ru.solodkov.voipadmin.service.OptionService;
import ru.solodkov.voipadmin.service.dto.OptionDTO;
import ru.solodkov.voipadmin.service.mapper.OptionMapper;

/**
 * Integration tests for the {@link OptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OptionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCR = "AAAAAAAAAA";
    private static final String UPDATED_DESCR = "BBBBBBBBBB";

    private static final OptionValueType DEFAULT_VALUE_TYPE = OptionValueType.TEXT;
    private static final OptionValueType UPDATED_VALUE_TYPE = OptionValueType.SELECT;

    private static final Boolean DEFAULT_MULTIPLE = false;
    private static final Boolean UPDATED_MULTIPLE = true;

    private static final String ENTITY_API_URL = "/api/options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OptionRepository optionRepository;

    @Mock
    private OptionRepository optionRepositoryMock;

    @Autowired
    private OptionMapper optionMapper;

    @Mock
    private OptionService optionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionMockMvc;

    private Option option;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity(EntityManager em) {
        Option option = new Option().code(DEFAULT_CODE).descr(DEFAULT_DESCR).valueType(DEFAULT_VALUE_TYPE).multiple(DEFAULT_MULTIPLE);
        return option;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createUpdatedEntity(EntityManager em) {
        Option option = new Option().code(UPDATED_CODE).descr(UPDATED_DESCR).valueType(UPDATED_VALUE_TYPE).multiple(UPDATED_MULTIPLE);
        return option;
    }

    @BeforeEach
    public void initTest() {
        option = createEntity(em);
    }

    @Test
    @Transactional
    void createOption() throws Exception {
        int databaseSizeBeforeCreate = optionRepository.findAll().size();
        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);
        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionDTO)))
            .andExpect(status().isCreated());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate + 1);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOption.getDescr()).isEqualTo(DEFAULT_DESCR);
        assertThat(testOption.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testOption.getMultiple()).isEqualTo(DEFAULT_MULTIPLE);
    }

    @Test
    @Transactional
    void createOptionWithExistingId() throws Exception {
        // Create the Option with an existing ID
        option.setId(1L);
        OptionDTO optionDTO = optionMapper.toDto(option);

        int databaseSizeBeforeCreate = optionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOptions() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].descr").value(hasItem(DEFAULT_DESCR)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].multiple").value(hasItem(DEFAULT_MULTIPLE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(optionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(optionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(optionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(optionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get the option
        restOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, option.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(option.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.descr").value(DEFAULT_DESCR))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.multiple").value(DEFAULT_MULTIPLE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOption() throws Exception {
        // Get the option
        restOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option
        Option updatedOption = optionRepository.findById(option.getId()).get();
        // Disconnect from session so that the updates on updatedOption are not directly saved in db
        em.detach(updatedOption);
        updatedOption.code(UPDATED_CODE).descr(UPDATED_DESCR).valueType(UPDATED_VALUE_TYPE).multiple(UPDATED_MULTIPLE);
        OptionDTO optionDTO = optionMapper.toDto(updatedOption);

        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOption.getDescr()).isEqualTo(UPDATED_DESCR);
        assertThat(testOption.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOption.getMultiple()).isEqualTo(UPDATED_MULTIPLE);
    }

    @Test
    @Transactional
    void putNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(optionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.descr(UPDATED_DESCR).valueType(UPDATED_VALUE_TYPE);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOption.getDescr()).isEqualTo(UPDATED_DESCR);
        assertThat(testOption.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOption.getMultiple()).isEqualTo(DEFAULT_MULTIPLE);
    }

    @Test
    @Transactional
    void fullUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeUpdate = optionRepository.findAll().size();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.code(UPDATED_CODE).descr(UPDATED_DESCR).valueType(UPDATED_VALUE_TYPE).multiple(UPDATED_MULTIPLE);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
        Option testOption = optionList.get(optionList.size() - 1);
        assertThat(testOption.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOption.getDescr()).isEqualTo(UPDATED_DESCR);
        assertThat(testOption.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testOption.getMultiple()).isEqualTo(UPDATED_MULTIPLE);
    }

    @Test
    @Transactional
    void patchNonExistingOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, optionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOption() throws Exception {
        int databaseSizeBeforeUpdate = optionRepository.findAll().size();
        option.setId(count.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(optionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOption() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        int databaseSizeBeforeDelete = optionRepository.findAll().size();

        // Delete the option
        restOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, option.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Option> optionList = optionRepository.findAll();
        assertThat(optionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
