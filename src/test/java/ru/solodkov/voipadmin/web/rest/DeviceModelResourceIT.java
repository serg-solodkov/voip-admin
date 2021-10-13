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
import org.springframework.util.Base64Utils;
import ru.solodkov.voipadmin.IntegrationTest;
import ru.solodkov.voipadmin.domain.DeviceModel;
import ru.solodkov.voipadmin.domain.enumeration.DeviceType;
import ru.solodkov.voipadmin.repository.DeviceModelRepository;
import ru.solodkov.voipadmin.service.DeviceModelService;
import ru.solodkov.voipadmin.service.dto.DeviceModelDTO;
import ru.solodkov.voipadmin.service.mapper.DeviceModelMapper;

/**
 * Integration tests for the {@link DeviceModelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeviceModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CONFIGURABLE = false;
    private static final Boolean UPDATED_IS_CONFIGURABLE = true;

    private static final Integer DEFAULT_LINES_COUNT = 1;
    private static final Integer UPDATED_LINES_COUNT = 2;

    private static final byte[] DEFAULT_CONFIG_TEMPLATE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONFIG_TEMPLATE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONFIG_TEMPLATE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONFIG_TEMPLATE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_FIRMWARE_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FIRMWARE_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FIRMWARE_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FIRMWARE_FILE_CONTENT_TYPE = "image/png";

    private static final DeviceType DEFAULT_DEVICE_TYPE = DeviceType.IPPHONE;
    private static final DeviceType UPDATED_DEVICE_TYPE = DeviceType.IPGATEWAY;

    private static final String ENTITY_API_URL = "/api/device-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceModelRepository deviceModelRepository;

    @Mock
    private DeviceModelRepository deviceModelRepositoryMock;

    @Autowired
    private DeviceModelMapper deviceModelMapper;

    @Mock
    private DeviceModelService deviceModelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceModelMockMvc;

    private DeviceModel deviceModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceModel createEntity(EntityManager em) {
        DeviceModel deviceModel = new DeviceModel()
            .name(DEFAULT_NAME)
            .isConfigurable(DEFAULT_IS_CONFIGURABLE)
            .linesCount(DEFAULT_LINES_COUNT)
            .configTemplate(DEFAULT_CONFIG_TEMPLATE)
            .configTemplateContentType(DEFAULT_CONFIG_TEMPLATE_CONTENT_TYPE)
            .firmwareFile(DEFAULT_FIRMWARE_FILE)
            .firmwareFileContentType(DEFAULT_FIRMWARE_FILE_CONTENT_TYPE)
            .deviceType(DEFAULT_DEVICE_TYPE);
        return deviceModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceModel createUpdatedEntity(EntityManager em) {
        DeviceModel deviceModel = new DeviceModel()
            .name(UPDATED_NAME)
            .isConfigurable(UPDATED_IS_CONFIGURABLE)
            .linesCount(UPDATED_LINES_COUNT)
            .configTemplate(UPDATED_CONFIG_TEMPLATE)
            .configTemplateContentType(UPDATED_CONFIG_TEMPLATE_CONTENT_TYPE)
            .firmwareFile(UPDATED_FIRMWARE_FILE)
            .firmwareFileContentType(UPDATED_FIRMWARE_FILE_CONTENT_TYPE)
            .deviceType(UPDATED_DEVICE_TYPE);
        return deviceModel;
    }

    @BeforeEach
    public void initTest() {
        deviceModel = createEntity(em);
    }

    @Test
    @Transactional
    void createDeviceModel() throws Exception {
        int databaseSizeBeforeCreate = deviceModelRepository.findAll().size();
        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);
        restDeviceModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceModel testDeviceModel = deviceModelList.get(deviceModelList.size() - 1);
        assertThat(testDeviceModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeviceModel.getIsConfigurable()).isEqualTo(DEFAULT_IS_CONFIGURABLE);
        assertThat(testDeviceModel.getLinesCount()).isEqualTo(DEFAULT_LINES_COUNT);
        assertThat(testDeviceModel.getConfigTemplate()).isEqualTo(DEFAULT_CONFIG_TEMPLATE);
        assertThat(testDeviceModel.getConfigTemplateContentType()).isEqualTo(DEFAULT_CONFIG_TEMPLATE_CONTENT_TYPE);
        assertThat(testDeviceModel.getFirmwareFile()).isEqualTo(DEFAULT_FIRMWARE_FILE);
        assertThat(testDeviceModel.getFirmwareFileContentType()).isEqualTo(DEFAULT_FIRMWARE_FILE_CONTENT_TYPE);
        assertThat(testDeviceModel.getDeviceType()).isEqualTo(DEFAULT_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void createDeviceModelWithExistingId() throws Exception {
        // Create the DeviceModel with an existing ID
        deviceModel.setId(1L);
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        int databaseSizeBeforeCreate = deviceModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceModelRepository.findAll().size();
        // set the field null
        deviceModel.setName(null);

        // Create the DeviceModel, which fails.
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        restDeviceModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsConfigurableIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceModelRepository.findAll().size();
        // set the field null
        deviceModel.setIsConfigurable(null);

        // Create the DeviceModel, which fails.
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        restDeviceModelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeviceModels() throws Exception {
        // Initialize the database
        deviceModelRepository.saveAndFlush(deviceModel);

        // Get all the deviceModelList
        restDeviceModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isConfigurable").value(hasItem(DEFAULT_IS_CONFIGURABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].linesCount").value(hasItem(DEFAULT_LINES_COUNT)))
            .andExpect(jsonPath("$.[*].configTemplateContentType").value(hasItem(DEFAULT_CONFIG_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].configTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONFIG_TEMPLATE))))
            .andExpect(jsonPath("$.[*].firmwareFileContentType").value(hasItem(DEFAULT_FIRMWARE_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].firmwareFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_FIRMWARE_FILE))))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeviceModelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(deviceModelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deviceModelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeviceModelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deviceModelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deviceModelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDeviceModel() throws Exception {
        // Initialize the database
        deviceModelRepository.saveAndFlush(deviceModel);

        // Get the deviceModel
        restDeviceModelMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isConfigurable").value(DEFAULT_IS_CONFIGURABLE.booleanValue()))
            .andExpect(jsonPath("$.linesCount").value(DEFAULT_LINES_COUNT))
            .andExpect(jsonPath("$.configTemplateContentType").value(DEFAULT_CONFIG_TEMPLATE_CONTENT_TYPE))
            .andExpect(jsonPath("$.configTemplate").value(Base64Utils.encodeToString(DEFAULT_CONFIG_TEMPLATE)))
            .andExpect(jsonPath("$.firmwareFileContentType").value(DEFAULT_FIRMWARE_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.firmwareFile").value(Base64Utils.encodeToString(DEFAULT_FIRMWARE_FILE)))
            .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeviceModel() throws Exception {
        // Get the deviceModel
        restDeviceModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeviceModel() throws Exception {
        // Initialize the database
        deviceModelRepository.saveAndFlush(deviceModel);

        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();

        // Update the deviceModel
        DeviceModel updatedDeviceModel = deviceModelRepository.findById(deviceModel.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceModel are not directly saved in db
        em.detach(updatedDeviceModel);
        updatedDeviceModel
            .name(UPDATED_NAME)
            .isConfigurable(UPDATED_IS_CONFIGURABLE)
            .linesCount(UPDATED_LINES_COUNT)
            .configTemplate(UPDATED_CONFIG_TEMPLATE)
            .configTemplateContentType(UPDATED_CONFIG_TEMPLATE_CONTENT_TYPE)
            .firmwareFile(UPDATED_FIRMWARE_FILE)
            .firmwareFileContentType(UPDATED_FIRMWARE_FILE_CONTENT_TYPE)
            .deviceType(UPDATED_DEVICE_TYPE);
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(updatedDeviceModel);

        restDeviceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
        DeviceModel testDeviceModel = deviceModelList.get(deviceModelList.size() - 1);
        assertThat(testDeviceModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeviceModel.getIsConfigurable()).isEqualTo(UPDATED_IS_CONFIGURABLE);
        assertThat(testDeviceModel.getLinesCount()).isEqualTo(UPDATED_LINES_COUNT);
        assertThat(testDeviceModel.getConfigTemplate()).isEqualTo(UPDATED_CONFIG_TEMPLATE);
        assertThat(testDeviceModel.getConfigTemplateContentType()).isEqualTo(UPDATED_CONFIG_TEMPLATE_CONTENT_TYPE);
        assertThat(testDeviceModel.getFirmwareFile()).isEqualTo(UPDATED_FIRMWARE_FILE);
        assertThat(testDeviceModel.getFirmwareFileContentType()).isEqualTo(UPDATED_FIRMWARE_FILE_CONTENT_TYPE);
        assertThat(testDeviceModel.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDeviceModel() throws Exception {
        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();
        deviceModel.setId(count.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceModel() throws Exception {
        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();
        deviceModel.setId(count.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceModel() throws Exception {
        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();
        deviceModel.setId(count.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceModelWithPatch() throws Exception {
        // Initialize the database
        deviceModelRepository.saveAndFlush(deviceModel);

        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();

        // Update the deviceModel using partial update
        DeviceModel partialUpdatedDeviceModel = new DeviceModel();
        partialUpdatedDeviceModel.setId(deviceModel.getId());

        partialUpdatedDeviceModel.name(UPDATED_NAME).deviceType(UPDATED_DEVICE_TYPE);

        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceModel))
            )
            .andExpect(status().isOk());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
        DeviceModel testDeviceModel = deviceModelList.get(deviceModelList.size() - 1);
        assertThat(testDeviceModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeviceModel.getIsConfigurable()).isEqualTo(DEFAULT_IS_CONFIGURABLE);
        assertThat(testDeviceModel.getLinesCount()).isEqualTo(DEFAULT_LINES_COUNT);
        assertThat(testDeviceModel.getConfigTemplate()).isEqualTo(DEFAULT_CONFIG_TEMPLATE);
        assertThat(testDeviceModel.getConfigTemplateContentType()).isEqualTo(DEFAULT_CONFIG_TEMPLATE_CONTENT_TYPE);
        assertThat(testDeviceModel.getFirmwareFile()).isEqualTo(DEFAULT_FIRMWARE_FILE);
        assertThat(testDeviceModel.getFirmwareFileContentType()).isEqualTo(DEFAULT_FIRMWARE_FILE_CONTENT_TYPE);
        assertThat(testDeviceModel.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDeviceModelWithPatch() throws Exception {
        // Initialize the database
        deviceModelRepository.saveAndFlush(deviceModel);

        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();

        // Update the deviceModel using partial update
        DeviceModel partialUpdatedDeviceModel = new DeviceModel();
        partialUpdatedDeviceModel.setId(deviceModel.getId());

        partialUpdatedDeviceModel
            .name(UPDATED_NAME)
            .isConfigurable(UPDATED_IS_CONFIGURABLE)
            .linesCount(UPDATED_LINES_COUNT)
            .configTemplate(UPDATED_CONFIG_TEMPLATE)
            .configTemplateContentType(UPDATED_CONFIG_TEMPLATE_CONTENT_TYPE)
            .firmwareFile(UPDATED_FIRMWARE_FILE)
            .firmwareFileContentType(UPDATED_FIRMWARE_FILE_CONTENT_TYPE)
            .deviceType(UPDATED_DEVICE_TYPE);

        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeviceModel))
            )
            .andExpect(status().isOk());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
        DeviceModel testDeviceModel = deviceModelList.get(deviceModelList.size() - 1);
        assertThat(testDeviceModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeviceModel.getIsConfigurable()).isEqualTo(UPDATED_IS_CONFIGURABLE);
        assertThat(testDeviceModel.getLinesCount()).isEqualTo(UPDATED_LINES_COUNT);
        assertThat(testDeviceModel.getConfigTemplate()).isEqualTo(UPDATED_CONFIG_TEMPLATE);
        assertThat(testDeviceModel.getConfigTemplateContentType()).isEqualTo(UPDATED_CONFIG_TEMPLATE_CONTENT_TYPE);
        assertThat(testDeviceModel.getFirmwareFile()).isEqualTo(UPDATED_FIRMWARE_FILE);
        assertThat(testDeviceModel.getFirmwareFileContentType()).isEqualTo(UPDATED_FIRMWARE_FILE_CONTENT_TYPE);
        assertThat(testDeviceModel.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDeviceModel() throws Exception {
        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();
        deviceModel.setId(count.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceModel() throws Exception {
        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();
        deviceModel.setId(count.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceModel() throws Exception {
        int databaseSizeBeforeUpdate = deviceModelRepository.findAll().size();
        deviceModel.setId(count.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceModel in the database
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceModel() throws Exception {
        // Initialize the database
        deviceModelRepository.saveAndFlush(deviceModel);

        int databaseSizeBeforeDelete = deviceModelRepository.findAll().size();

        // Delete the deviceModel
        restDeviceModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceModel> deviceModelList = deviceModelRepository.findAll();
        assertThat(deviceModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
