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
import ru.solodkov.voipadmin.domain.ResponsiblePerson;
import ru.solodkov.voipadmin.repository.ResponsiblePersonRepository;
import ru.solodkov.voipadmin.service.dto.ResponsiblePersonDTO;
import ru.solodkov.voipadmin.service.mapper.ResponsiblePersonMapper;

/**
 * Integration tests for the {@link ResponsiblePersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResponsiblePersonResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM = "AAAAAAAAAA";
    private static final String UPDATED_ROOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responsible-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResponsiblePersonRepository responsiblePersonRepository;

    @Autowired
    private ResponsiblePersonMapper responsiblePersonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResponsiblePersonMockMvc;

    private ResponsiblePerson responsiblePerson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsiblePerson createEntity(EntityManager em) {
        ResponsiblePerson responsiblePerson = new ResponsiblePerson()
            .code(DEFAULT_CODE)
            .firstName(DEFAULT_FIRST_NAME)
            .secondName(DEFAULT_SECOND_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .position(DEFAULT_POSITION)
            .room(DEFAULT_ROOM);
        return responsiblePerson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResponsiblePerson createUpdatedEntity(EntityManager em) {
        ResponsiblePerson responsiblePerson = new ResponsiblePerson()
            .code(UPDATED_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .secondName(UPDATED_SECOND_NAME)
            .lastName(UPDATED_LAST_NAME)
            .position(UPDATED_POSITION)
            .room(UPDATED_ROOM);
        return responsiblePerson;
    }

    @BeforeEach
    public void initTest() {
        responsiblePerson = createEntity(em);
    }

    @Test
    @Transactional
    void createResponsiblePerson() throws Exception {
        int databaseSizeBeforeCreate = responsiblePersonRepository.findAll().size();
        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);
        restResponsiblePersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeCreate + 1);
        ResponsiblePerson testResponsiblePerson = responsiblePersonList.get(responsiblePersonList.size() - 1);
        assertThat(testResponsiblePerson.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testResponsiblePerson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testResponsiblePerson.getSecondName()).isEqualTo(DEFAULT_SECOND_NAME);
        assertThat(testResponsiblePerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testResponsiblePerson.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testResponsiblePerson.getRoom()).isEqualTo(DEFAULT_ROOM);
    }

    @Test
    @Transactional
    void createResponsiblePersonWithExistingId() throws Exception {
        // Create the ResponsiblePerson with an existing ID
        responsiblePerson.setId(1L);
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        int databaseSizeBeforeCreate = responsiblePersonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsiblePersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsiblePersonRepository.findAll().size();
        // set the field null
        responsiblePerson.setCode(null);

        // Create the ResponsiblePerson, which fails.
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        restResponsiblePersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsiblePersonRepository.findAll().size();
        // set the field null
        responsiblePerson.setFirstName(null);

        // Create the ResponsiblePerson, which fails.
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        restResponsiblePersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = responsiblePersonRepository.findAll().size();
        // set the field null
        responsiblePerson.setLastName(null);

        // Create the ResponsiblePerson, which fails.
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        restResponsiblePersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResponsiblePeople() throws Exception {
        // Initialize the database
        responsiblePersonRepository.saveAndFlush(responsiblePerson);

        // Get all the responsiblePersonList
        restResponsiblePersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsiblePerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].secondName").value(hasItem(DEFAULT_SECOND_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].room").value(hasItem(DEFAULT_ROOM)));
    }

    @Test
    @Transactional
    void getResponsiblePerson() throws Exception {
        // Initialize the database
        responsiblePersonRepository.saveAndFlush(responsiblePerson);

        // Get the responsiblePerson
        restResponsiblePersonMockMvc
            .perform(get(ENTITY_API_URL_ID, responsiblePerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(responsiblePerson.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.secondName").value(DEFAULT_SECOND_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.room").value(DEFAULT_ROOM));
    }

    @Test
    @Transactional
    void getNonExistingResponsiblePerson() throws Exception {
        // Get the responsiblePerson
        restResponsiblePersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResponsiblePerson() throws Exception {
        // Initialize the database
        responsiblePersonRepository.saveAndFlush(responsiblePerson);

        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();

        // Update the responsiblePerson
        ResponsiblePerson updatedResponsiblePerson = responsiblePersonRepository.findById(responsiblePerson.getId()).get();
        // Disconnect from session so that the updates on updatedResponsiblePerson are not directly saved in db
        em.detach(updatedResponsiblePerson);
        updatedResponsiblePerson
            .code(UPDATED_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .secondName(UPDATED_SECOND_NAME)
            .lastName(UPDATED_LAST_NAME)
            .position(UPDATED_POSITION)
            .room(UPDATED_ROOM);
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(updatedResponsiblePerson);

        restResponsiblePersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsiblePersonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isOk());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
        ResponsiblePerson testResponsiblePerson = responsiblePersonList.get(responsiblePersonList.size() - 1);
        assertThat(testResponsiblePerson.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testResponsiblePerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testResponsiblePerson.getSecondName()).isEqualTo(UPDATED_SECOND_NAME);
        assertThat(testResponsiblePerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testResponsiblePerson.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testResponsiblePerson.getRoom()).isEqualTo(UPDATED_ROOM);
    }

    @Test
    @Transactional
    void putNonExistingResponsiblePerson() throws Exception {
        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();
        responsiblePerson.setId(count.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsiblePersonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResponsiblePerson() throws Exception {
        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();
        responsiblePerson.setId(count.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResponsiblePerson() throws Exception {
        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();
        responsiblePerson.setId(count.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResponsiblePersonWithPatch() throws Exception {
        // Initialize the database
        responsiblePersonRepository.saveAndFlush(responsiblePerson);

        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();

        // Update the responsiblePerson using partial update
        ResponsiblePerson partialUpdatedResponsiblePerson = new ResponsiblePerson();
        partialUpdatedResponsiblePerson.setId(responsiblePerson.getId());

        partialUpdatedResponsiblePerson.firstName(UPDATED_FIRST_NAME).secondName(UPDATED_SECOND_NAME).room(UPDATED_ROOM);

        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsiblePerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsiblePerson))
            )
            .andExpect(status().isOk());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
        ResponsiblePerson testResponsiblePerson = responsiblePersonList.get(responsiblePersonList.size() - 1);
        assertThat(testResponsiblePerson.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testResponsiblePerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testResponsiblePerson.getSecondName()).isEqualTo(UPDATED_SECOND_NAME);
        assertThat(testResponsiblePerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testResponsiblePerson.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testResponsiblePerson.getRoom()).isEqualTo(UPDATED_ROOM);
    }

    @Test
    @Transactional
    void fullUpdateResponsiblePersonWithPatch() throws Exception {
        // Initialize the database
        responsiblePersonRepository.saveAndFlush(responsiblePerson);

        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();

        // Update the responsiblePerson using partial update
        ResponsiblePerson partialUpdatedResponsiblePerson = new ResponsiblePerson();
        partialUpdatedResponsiblePerson.setId(responsiblePerson.getId());

        partialUpdatedResponsiblePerson
            .code(UPDATED_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .secondName(UPDATED_SECOND_NAME)
            .lastName(UPDATED_LAST_NAME)
            .position(UPDATED_POSITION)
            .room(UPDATED_ROOM);

        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsiblePerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResponsiblePerson))
            )
            .andExpect(status().isOk());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
        ResponsiblePerson testResponsiblePerson = responsiblePersonList.get(responsiblePersonList.size() - 1);
        assertThat(testResponsiblePerson.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testResponsiblePerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testResponsiblePerson.getSecondName()).isEqualTo(UPDATED_SECOND_NAME);
        assertThat(testResponsiblePerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testResponsiblePerson.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testResponsiblePerson.getRoom()).isEqualTo(UPDATED_ROOM);
    }

    @Test
    @Transactional
    void patchNonExistingResponsiblePerson() throws Exception {
        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();
        responsiblePerson.setId(count.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, responsiblePersonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResponsiblePerson() throws Exception {
        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();
        responsiblePerson.setId(count.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResponsiblePerson() throws Exception {
        int databaseSizeBeforeUpdate = responsiblePersonRepository.findAll().size();
        responsiblePerson.setId(count.incrementAndGet());

        // Create the ResponsiblePerson
        ResponsiblePersonDTO responsiblePersonDTO = responsiblePersonMapper.toDto(responsiblePerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsiblePersonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(responsiblePersonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResponsiblePerson in the database
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResponsiblePerson() throws Exception {
        // Initialize the database
        responsiblePersonRepository.saveAndFlush(responsiblePerson);

        int databaseSizeBeforeDelete = responsiblePersonRepository.findAll().size();

        // Delete the responsiblePerson
        restResponsiblePersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, responsiblePerson.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResponsiblePerson> responsiblePersonList = responsiblePersonRepository.findAll();
        assertThat(responsiblePersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
