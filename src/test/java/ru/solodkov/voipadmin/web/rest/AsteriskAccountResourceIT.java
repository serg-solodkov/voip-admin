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
import ru.solodkov.voipadmin.domain.AsteriskAccount;
import ru.solodkov.voipadmin.repository.AsteriskAccountRepository;
import ru.solodkov.voipadmin.service.dto.AsteriskAccountDTO;
import ru.solodkov.voipadmin.service.mapper.AsteriskAccountMapper;

/**
 * Integration tests for the {@link AsteriskAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AsteriskAccountResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ASTERISK_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASTERISK_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/asterisk-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AsteriskAccountRepository asteriskAccountRepository;

    @Autowired
    private AsteriskAccountMapper asteriskAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAsteriskAccountMockMvc;

    private AsteriskAccount asteriskAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsteriskAccount createEntity(EntityManager em) {
        AsteriskAccount asteriskAccount = new AsteriskAccount().username(DEFAULT_USERNAME).asteriskId(DEFAULT_ASTERISK_ID);
        return asteriskAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AsteriskAccount createUpdatedEntity(EntityManager em) {
        AsteriskAccount asteriskAccount = new AsteriskAccount().username(UPDATED_USERNAME).asteriskId(UPDATED_ASTERISK_ID);
        return asteriskAccount;
    }

    @BeforeEach
    public void initTest() {
        asteriskAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createAsteriskAccount() throws Exception {
        int databaseSizeBeforeCreate = asteriskAccountRepository.findAll().size();
        // Create the AsteriskAccount
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);
        restAsteriskAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeCreate + 1);
        AsteriskAccount testAsteriskAccount = asteriskAccountList.get(asteriskAccountList.size() - 1);
        assertThat(testAsteriskAccount.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testAsteriskAccount.getAsteriskId()).isEqualTo(DEFAULT_ASTERISK_ID);
    }

    @Test
    @Transactional
    void createAsteriskAccountWithExistingId() throws Exception {
        // Create the AsteriskAccount with an existing ID
        asteriskAccount.setId(1L);
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);

        int databaseSizeBeforeCreate = asteriskAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAsteriskAccountMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAsteriskAccounts() throws Exception {
        // Initialize the database
        asteriskAccountRepository.saveAndFlush(asteriskAccount);

        // Get all the asteriskAccountList
        restAsteriskAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(asteriskAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].asteriskId").value(hasItem(DEFAULT_ASTERISK_ID)));
    }

    @Test
    @Transactional
    void getAsteriskAccount() throws Exception {
        // Initialize the database
        asteriskAccountRepository.saveAndFlush(asteriskAccount);

        // Get the asteriskAccount
        restAsteriskAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, asteriskAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(asteriskAccount.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.asteriskId").value(DEFAULT_ASTERISK_ID));
    }

    @Test
    @Transactional
    void getNonExistingAsteriskAccount() throws Exception {
        // Get the asteriskAccount
        restAsteriskAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAsteriskAccount() throws Exception {
        // Initialize the database
        asteriskAccountRepository.saveAndFlush(asteriskAccount);

        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();

        // Update the asteriskAccount
        AsteriskAccount updatedAsteriskAccount = asteriskAccountRepository.findById(asteriskAccount.getId()).get();
        // Disconnect from session so that the updates on updatedAsteriskAccount are not directly saved in db
        em.detach(updatedAsteriskAccount);
        updatedAsteriskAccount.username(UPDATED_USERNAME).asteriskId(UPDATED_ASTERISK_ID);
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(updatedAsteriskAccount);

        restAsteriskAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, asteriskAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
        AsteriskAccount testAsteriskAccount = asteriskAccountList.get(asteriskAccountList.size() - 1);
        assertThat(testAsteriskAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testAsteriskAccount.getAsteriskId()).isEqualTo(UPDATED_ASTERISK_ID);
    }

    @Test
    @Transactional
    void putNonExistingAsteriskAccount() throws Exception {
        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();
        asteriskAccount.setId(count.incrementAndGet());

        // Create the AsteriskAccount
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsteriskAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, asteriskAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAsteriskAccount() throws Exception {
        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();
        asteriskAccount.setId(count.incrementAndGet());

        // Create the AsteriskAccount
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsteriskAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAsteriskAccount() throws Exception {
        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();
        asteriskAccount.setId(count.incrementAndGet());

        // Create the AsteriskAccount
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsteriskAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAsteriskAccountWithPatch() throws Exception {
        // Initialize the database
        asteriskAccountRepository.saveAndFlush(asteriskAccount);

        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();

        // Update the asteriskAccount using partial update
        AsteriskAccount partialUpdatedAsteriskAccount = new AsteriskAccount();
        partialUpdatedAsteriskAccount.setId(asteriskAccount.getId());

        partialUpdatedAsteriskAccount.username(UPDATED_USERNAME).asteriskId(UPDATED_ASTERISK_ID);

        restAsteriskAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsteriskAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsteriskAccount))
            )
            .andExpect(status().isOk());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
        AsteriskAccount testAsteriskAccount = asteriskAccountList.get(asteriskAccountList.size() - 1);
        assertThat(testAsteriskAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testAsteriskAccount.getAsteriskId()).isEqualTo(UPDATED_ASTERISK_ID);
    }

    @Test
    @Transactional
    void fullUpdateAsteriskAccountWithPatch() throws Exception {
        // Initialize the database
        asteriskAccountRepository.saveAndFlush(asteriskAccount);

        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();

        // Update the asteriskAccount using partial update
        AsteriskAccount partialUpdatedAsteriskAccount = new AsteriskAccount();
        partialUpdatedAsteriskAccount.setId(asteriskAccount.getId());

        partialUpdatedAsteriskAccount.username(UPDATED_USERNAME).asteriskId(UPDATED_ASTERISK_ID);

        restAsteriskAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsteriskAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsteriskAccount))
            )
            .andExpect(status().isOk());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
        AsteriskAccount testAsteriskAccount = asteriskAccountList.get(asteriskAccountList.size() - 1);
        assertThat(testAsteriskAccount.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testAsteriskAccount.getAsteriskId()).isEqualTo(UPDATED_ASTERISK_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAsteriskAccount() throws Exception {
        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();
        asteriskAccount.setId(count.incrementAndGet());

        // Create the AsteriskAccount
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAsteriskAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, asteriskAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAsteriskAccount() throws Exception {
        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();
        asteriskAccount.setId(count.incrementAndGet());

        // Create the AsteriskAccount
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsteriskAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAsteriskAccount() throws Exception {
        int databaseSizeBeforeUpdate = asteriskAccountRepository.findAll().size();
        asteriskAccount.setId(count.incrementAndGet());

        // Create the AsteriskAccount
        AsteriskAccountDTO asteriskAccountDTO = asteriskAccountMapper.toDto(asteriskAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAsteriskAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(asteriskAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AsteriskAccount in the database
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAsteriskAccount() throws Exception {
        // Initialize the database
        asteriskAccountRepository.saveAndFlush(asteriskAccount);

        int databaseSizeBeforeDelete = asteriskAccountRepository.findAll().size();

        // Delete the asteriskAccount
        restAsteriskAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, asteriskAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AsteriskAccount> asteriskAccountList = asteriskAccountRepository.findAll();
        assertThat(asteriskAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
