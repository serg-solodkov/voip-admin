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
import ru.solodkov.voipadmin.domain.DeviceModel;
import ru.solodkov.voipadmin.domain.Option;
import ru.solodkov.voipadmin.domain.OptionValue;
import ru.solodkov.voipadmin.domain.Vendor;
import ru.solodkov.voipadmin.domain.enumeration.OptionValueType;
import ru.solodkov.voipadmin.repository.OptionRepository;
import ru.solodkov.voipadmin.service.OptionService;
import ru.solodkov.voipadmin.service.criteria.OptionCriteria;
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
    void getOptionsByIdFiltering() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        Long id = option.getId();

        defaultOptionShouldBeFound("id.equals=" + id);
        defaultOptionShouldNotBeFound("id.notEquals=" + id);

        defaultOptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOptionShouldNotBeFound("id.greaterThan=" + id);

        defaultOptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where code equals to DEFAULT_CODE
        defaultOptionShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the optionList where code equals to UPDATED_CODE
        defaultOptionShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where code not equals to DEFAULT_CODE
        defaultOptionShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the optionList where code not equals to UPDATED_CODE
        defaultOptionShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where code in DEFAULT_CODE or UPDATED_CODE
        defaultOptionShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the optionList where code equals to UPDATED_CODE
        defaultOptionShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where code is not null
        defaultOptionShouldBeFound("code.specified=true");

        // Get all the optionList where code is null
        defaultOptionShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where code contains DEFAULT_CODE
        defaultOptionShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the optionList where code contains UPDATED_CODE
        defaultOptionShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where code does not contain DEFAULT_CODE
        defaultOptionShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the optionList where code does not contain UPDATED_CODE
        defaultOptionShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where descr equals to DEFAULT_DESCR
        defaultOptionShouldBeFound("descr.equals=" + DEFAULT_DESCR);

        // Get all the optionList where descr equals to UPDATED_DESCR
        defaultOptionShouldNotBeFound("descr.equals=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where descr not equals to DEFAULT_DESCR
        defaultOptionShouldNotBeFound("descr.notEquals=" + DEFAULT_DESCR);

        // Get all the optionList where descr not equals to UPDATED_DESCR
        defaultOptionShouldBeFound("descr.notEquals=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where descr in DEFAULT_DESCR or UPDATED_DESCR
        defaultOptionShouldBeFound("descr.in=" + DEFAULT_DESCR + "," + UPDATED_DESCR);

        // Get all the optionList where descr equals to UPDATED_DESCR
        defaultOptionShouldNotBeFound("descr.in=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where descr is not null
        defaultOptionShouldBeFound("descr.specified=true");

        // Get all the optionList where descr is null
        defaultOptionShouldNotBeFound("descr.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByDescrContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where descr contains DEFAULT_DESCR
        defaultOptionShouldBeFound("descr.contains=" + DEFAULT_DESCR);

        // Get all the optionList where descr contains UPDATED_DESCR
        defaultOptionShouldNotBeFound("descr.contains=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrNotContainsSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where descr does not contain DEFAULT_DESCR
        defaultOptionShouldNotBeFound("descr.doesNotContain=" + DEFAULT_DESCR);

        // Get all the optionList where descr does not contain UPDATED_DESCR
        defaultOptionShouldBeFound("descr.doesNotContain=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where valueType equals to DEFAULT_VALUE_TYPE
        defaultOptionShouldBeFound("valueType.equals=" + DEFAULT_VALUE_TYPE);

        // Get all the optionList where valueType equals to UPDATED_VALUE_TYPE
        defaultOptionShouldNotBeFound("valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllOptionsByValueTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where valueType not equals to DEFAULT_VALUE_TYPE
        defaultOptionShouldNotBeFound("valueType.notEquals=" + DEFAULT_VALUE_TYPE);

        // Get all the optionList where valueType not equals to UPDATED_VALUE_TYPE
        defaultOptionShouldBeFound("valueType.notEquals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllOptionsByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where valueType in DEFAULT_VALUE_TYPE or UPDATED_VALUE_TYPE
        defaultOptionShouldBeFound("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE);

        // Get all the optionList where valueType equals to UPDATED_VALUE_TYPE
        defaultOptionShouldNotBeFound("valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllOptionsByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where valueType is not null
        defaultOptionShouldBeFound("valueType.specified=true");

        // Get all the optionList where valueType is null
        defaultOptionShouldNotBeFound("valueType.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByMultipleIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where multiple equals to DEFAULT_MULTIPLE
        defaultOptionShouldBeFound("multiple.equals=" + DEFAULT_MULTIPLE);

        // Get all the optionList where multiple equals to UPDATED_MULTIPLE
        defaultOptionShouldNotBeFound("multiple.equals=" + UPDATED_MULTIPLE);
    }

    @Test
    @Transactional
    void getAllOptionsByMultipleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where multiple not equals to DEFAULT_MULTIPLE
        defaultOptionShouldNotBeFound("multiple.notEquals=" + DEFAULT_MULTIPLE);

        // Get all the optionList where multiple not equals to UPDATED_MULTIPLE
        defaultOptionShouldBeFound("multiple.notEquals=" + UPDATED_MULTIPLE);
    }

    @Test
    @Transactional
    void getAllOptionsByMultipleIsInShouldWork() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where multiple in DEFAULT_MULTIPLE or UPDATED_MULTIPLE
        defaultOptionShouldBeFound("multiple.in=" + DEFAULT_MULTIPLE + "," + UPDATED_MULTIPLE);

        // Get all the optionList where multiple equals to UPDATED_MULTIPLE
        defaultOptionShouldNotBeFound("multiple.in=" + UPDATED_MULTIPLE);
    }

    @Test
    @Transactional
    void getAllOptionsByMultipleIsNullOrNotNull() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);

        // Get all the optionList where multiple is not null
        defaultOptionShouldBeFound("multiple.specified=true");

        // Get all the optionList where multiple is null
        defaultOptionShouldNotBeFound("multiple.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByPossibleValuesIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);
        OptionValue possibleValues = OptionValueResourceIT.createEntity(em);
        em.persist(possibleValues);
        em.flush();
        option.addPossibleValues(possibleValues);
        optionRepository.saveAndFlush(option);
        Long possibleValuesId = possibleValues.getId();

        // Get all the optionList where possibleValues equals to possibleValuesId
        defaultOptionShouldBeFound("possibleValuesId.equals=" + possibleValuesId);

        // Get all the optionList where possibleValues equals to (possibleValuesId + 1)
        defaultOptionShouldNotBeFound("possibleValuesId.equals=" + (possibleValuesId + 1));
    }

    @Test
    @Transactional
    void getAllOptionsByVendorsIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);
        Vendor vendors = VendorResourceIT.createEntity(em);
        em.persist(vendors);
        em.flush();
        option.addVendors(vendors);
        optionRepository.saveAndFlush(option);
        Long vendorsId = vendors.getId();

        // Get all the optionList where vendors equals to vendorsId
        defaultOptionShouldBeFound("vendorsId.equals=" + vendorsId);

        // Get all the optionList where vendors equals to (vendorsId + 1)
        defaultOptionShouldNotBeFound("vendorsId.equals=" + (vendorsId + 1));
    }

    @Test
    @Transactional
    void getAllOptionsByModelsIsEqualToSomething() throws Exception {
        // Initialize the database
        optionRepository.saveAndFlush(option);
        DeviceModel models = DeviceModelResourceIT.createEntity(em);
        em.persist(models);
        em.flush();
        option.addModels(models);
        optionRepository.saveAndFlush(option);
        Long modelsId = models.getId();

        // Get all the optionList where models equals to modelsId
        defaultOptionShouldBeFound("modelsId.equals=" + modelsId);

        // Get all the optionList where models equals to (modelsId + 1)
        defaultOptionShouldNotBeFound("modelsId.equals=" + (modelsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOptionShouldBeFound(String filter) throws Exception {
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].descr").value(hasItem(DEFAULT_DESCR)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].multiple").value(hasItem(DEFAULT_MULTIPLE.booleanValue())));

        // Check, that the count call also returns 1
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOptionShouldNotBeFound(String filter) throws Exception {
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
