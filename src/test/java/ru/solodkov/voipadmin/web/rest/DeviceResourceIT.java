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
import org.springframework.util.Base64Utils;
import ru.solodkov.voipadmin.IntegrationTest;
import ru.solodkov.voipadmin.domain.Device;
import ru.solodkov.voipadmin.domain.enumeration.ProvisioningMode;
import ru.solodkov.voipadmin.repository.DeviceRepository;
import ru.solodkov.voipadmin.service.dto.DeviceDTO;
import ru.solodkov.voipadmin.service.mapper.DeviceMapper;

/**
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final String DEFAULT_MAC = "AAAAAAAAAA";
    private static final String UPDATED_MAC = "BBBBBBBBBB";

    private static final String DEFAULT_INVENTORY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_HOSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_WEB_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_WEB_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DHCP_ENABLED = false;
    private static final Boolean UPDATED_DHCP_ENABLED = true;

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SUBNET_MASK = "AAAAAAAAAA";
    private static final String UPDATED_SUBNET_MASK = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_GW = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_GW = "BBBBBBBBBB";

    private static final String DEFAULT_DNS_1 = "AAAAAAAAAA";
    private static final String UPDATED_DNS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DNS_2 = "AAAAAAAAAA";
    private static final String UPDATED_DNS_2 = "BBBBBBBBBB";

    private static final ProvisioningMode DEFAULT_PROVISIONING_MODE = ProvisioningMode.FTP;
    private static final ProvisioningMode UPDATED_PROVISIONING_MODE = ProvisioningMode.TFTP;

    private static final String DEFAULT_PROVISIONING_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROVISIONING_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NTP_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_NTP_SERVER = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONFIGURATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONFIGURATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONFIGURATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONFIGURATION_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device()
            .mac(DEFAULT_MAC)
            .inventoryNumber(DEFAULT_INVENTORY_NUMBER)
            .location(DEFAULT_LOCATION)
            .hostname(DEFAULT_HOSTNAME)
            .webLogin(DEFAULT_WEB_LOGIN)
            .webPassword(DEFAULT_WEB_PASSWORD)
            .dhcpEnabled(DEFAULT_DHCP_ENABLED)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .subnetMask(DEFAULT_SUBNET_MASK)
            .defaultGw(DEFAULT_DEFAULT_GW)
            .dns1(DEFAULT_DNS_1)
            .dns2(DEFAULT_DNS_2)
            .provisioningMode(DEFAULT_PROVISIONING_MODE)
            .provisioningUrl(DEFAULT_PROVISIONING_URL)
            .ntpServer(DEFAULT_NTP_SERVER)
            .notes(DEFAULT_NOTES)
            .configuration(DEFAULT_CONFIGURATION)
            .configurationContentType(DEFAULT_CONFIGURATION_CONTENT_TYPE);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity(EntityManager em) {
        Device device = new Device()
            .mac(UPDATED_MAC)
            .inventoryNumber(UPDATED_INVENTORY_NUMBER)
            .location(UPDATED_LOCATION)
            .hostname(UPDATED_HOSTNAME)
            .webLogin(UPDATED_WEB_LOGIN)
            .webPassword(UPDATED_WEB_PASSWORD)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .subnetMask(UPDATED_SUBNET_MASK)
            .defaultGw(UPDATED_DEFAULT_GW)
            .dns1(UPDATED_DNS_1)
            .dns2(UPDATED_DNS_2)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL)
            .ntpServer(UPDATED_NTP_SERVER)
            .notes(UPDATED_NOTES)
            .configuration(UPDATED_CONFIGURATION)
            .configurationContentType(UPDATED_CONFIGURATION_CONTENT_TYPE);
        return device;
    }

    @BeforeEach
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();
        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getMac()).isEqualTo(DEFAULT_MAC);
        assertThat(testDevice.getInventoryNumber()).isEqualTo(DEFAULT_INVENTORY_NUMBER);
        assertThat(testDevice.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testDevice.getHostname()).isEqualTo(DEFAULT_HOSTNAME);
        assertThat(testDevice.getWebLogin()).isEqualTo(DEFAULT_WEB_LOGIN);
        assertThat(testDevice.getWebPassword()).isEqualTo(DEFAULT_WEB_PASSWORD);
        assertThat(testDevice.getDhcpEnabled()).isEqualTo(DEFAULT_DHCP_ENABLED);
        assertThat(testDevice.getIpAddress()).isEqualTo(DEFAULT_IP_ADDRESS);
        assertThat(testDevice.getSubnetMask()).isEqualTo(DEFAULT_SUBNET_MASK);
        assertThat(testDevice.getDefaultGw()).isEqualTo(DEFAULT_DEFAULT_GW);
        assertThat(testDevice.getDns1()).isEqualTo(DEFAULT_DNS_1);
        assertThat(testDevice.getDns2()).isEqualTo(DEFAULT_DNS_2);
        assertThat(testDevice.getProvisioningMode()).isEqualTo(DEFAULT_PROVISIONING_MODE);
        assertThat(testDevice.getProvisioningUrl()).isEqualTo(DEFAULT_PROVISIONING_URL);
        assertThat(testDevice.getNtpServer()).isEqualTo(DEFAULT_NTP_SERVER);
        assertThat(testDevice.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testDevice.getConfiguration()).isEqualTo(DEFAULT_CONFIGURATION);
        assertThat(testDevice.getConfigurationContentType()).isEqualTo(DEFAULT_CONFIGURATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMacIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setMac(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)))
            .andExpect(jsonPath("$.[*].inventoryNumber").value(hasItem(DEFAULT_INVENTORY_NUMBER)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME)))
            .andExpect(jsonPath("$.[*].webLogin").value(hasItem(DEFAULT_WEB_LOGIN)))
            .andExpect(jsonPath("$.[*].webPassword").value(hasItem(DEFAULT_WEB_PASSWORD)))
            .andExpect(jsonPath("$.[*].dhcpEnabled").value(hasItem(DEFAULT_DHCP_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].subnetMask").value(hasItem(DEFAULT_SUBNET_MASK)))
            .andExpect(jsonPath("$.[*].defaultGw").value(hasItem(DEFAULT_DEFAULT_GW)))
            .andExpect(jsonPath("$.[*].dns1").value(hasItem(DEFAULT_DNS_1)))
            .andExpect(jsonPath("$.[*].dns2").value(hasItem(DEFAULT_DNS_2)))
            .andExpect(jsonPath("$.[*].provisioningMode").value(hasItem(DEFAULT_PROVISIONING_MODE.toString())))
            .andExpect(jsonPath("$.[*].provisioningUrl").value(hasItem(DEFAULT_PROVISIONING_URL)))
            .andExpect(jsonPath("$.[*].ntpServer").value(hasItem(DEFAULT_NTP_SERVER)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].configurationContentType").value(hasItem(DEFAULT_CONFIGURATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONFIGURATION))));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.mac").value(DEFAULT_MAC))
            .andExpect(jsonPath("$.inventoryNumber").value(DEFAULT_INVENTORY_NUMBER))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME))
            .andExpect(jsonPath("$.webLogin").value(DEFAULT_WEB_LOGIN))
            .andExpect(jsonPath("$.webPassword").value(DEFAULT_WEB_PASSWORD))
            .andExpect(jsonPath("$.dhcpEnabled").value(DEFAULT_DHCP_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.subnetMask").value(DEFAULT_SUBNET_MASK))
            .andExpect(jsonPath("$.defaultGw").value(DEFAULT_DEFAULT_GW))
            .andExpect(jsonPath("$.dns1").value(DEFAULT_DNS_1))
            .andExpect(jsonPath("$.dns2").value(DEFAULT_DNS_2))
            .andExpect(jsonPath("$.provisioningMode").value(DEFAULT_PROVISIONING_MODE.toString()))
            .andExpect(jsonPath("$.provisioningUrl").value(DEFAULT_PROVISIONING_URL))
            .andExpect(jsonPath("$.ntpServer").value(DEFAULT_NTP_SERVER))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.configurationContentType").value(DEFAULT_CONFIGURATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.configuration").value(Base64Utils.encodeToString(DEFAULT_CONFIGURATION)));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice
            .mac(UPDATED_MAC)
            .inventoryNumber(UPDATED_INVENTORY_NUMBER)
            .location(UPDATED_LOCATION)
            .hostname(UPDATED_HOSTNAME)
            .webLogin(UPDATED_WEB_LOGIN)
            .webPassword(UPDATED_WEB_PASSWORD)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .subnetMask(UPDATED_SUBNET_MASK)
            .defaultGw(UPDATED_DEFAULT_GW)
            .dns1(UPDATED_DNS_1)
            .dns2(UPDATED_DNS_2)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL)
            .ntpServer(UPDATED_NTP_SERVER)
            .notes(UPDATED_NOTES)
            .configuration(UPDATED_CONFIGURATION)
            .configurationContentType(UPDATED_CONFIGURATION_CONTENT_TYPE);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getMac()).isEqualTo(UPDATED_MAC);
        assertThat(testDevice.getInventoryNumber()).isEqualTo(UPDATED_INVENTORY_NUMBER);
        assertThat(testDevice.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testDevice.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testDevice.getWebLogin()).isEqualTo(UPDATED_WEB_LOGIN);
        assertThat(testDevice.getWebPassword()).isEqualTo(UPDATED_WEB_PASSWORD);
        assertThat(testDevice.getDhcpEnabled()).isEqualTo(UPDATED_DHCP_ENABLED);
        assertThat(testDevice.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testDevice.getSubnetMask()).isEqualTo(UPDATED_SUBNET_MASK);
        assertThat(testDevice.getDefaultGw()).isEqualTo(UPDATED_DEFAULT_GW);
        assertThat(testDevice.getDns1()).isEqualTo(UPDATED_DNS_1);
        assertThat(testDevice.getDns2()).isEqualTo(UPDATED_DNS_2);
        assertThat(testDevice.getProvisioningMode()).isEqualTo(UPDATED_PROVISIONING_MODE);
        assertThat(testDevice.getProvisioningUrl()).isEqualTo(UPDATED_PROVISIONING_URL);
        assertThat(testDevice.getNtpServer()).isEqualTo(UPDATED_NTP_SERVER);
        assertThat(testDevice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDevice.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
        assertThat(testDevice.getConfigurationContentType()).isEqualTo(UPDATED_CONFIGURATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .hostname(UPDATED_HOSTNAME)
            .webLogin(UPDATED_WEB_LOGIN)
            .webPassword(UPDATED_WEB_PASSWORD)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .subnetMask(UPDATED_SUBNET_MASK)
            .dns1(UPDATED_DNS_1)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL)
            .ntpServer(UPDATED_NTP_SERVER)
            .notes(UPDATED_NOTES);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getMac()).isEqualTo(DEFAULT_MAC);
        assertThat(testDevice.getInventoryNumber()).isEqualTo(DEFAULT_INVENTORY_NUMBER);
        assertThat(testDevice.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testDevice.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testDevice.getWebLogin()).isEqualTo(UPDATED_WEB_LOGIN);
        assertThat(testDevice.getWebPassword()).isEqualTo(UPDATED_WEB_PASSWORD);
        assertThat(testDevice.getDhcpEnabled()).isEqualTo(UPDATED_DHCP_ENABLED);
        assertThat(testDevice.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testDevice.getSubnetMask()).isEqualTo(UPDATED_SUBNET_MASK);
        assertThat(testDevice.getDefaultGw()).isEqualTo(DEFAULT_DEFAULT_GW);
        assertThat(testDevice.getDns1()).isEqualTo(UPDATED_DNS_1);
        assertThat(testDevice.getDns2()).isEqualTo(DEFAULT_DNS_2);
        assertThat(testDevice.getProvisioningMode()).isEqualTo(UPDATED_PROVISIONING_MODE);
        assertThat(testDevice.getProvisioningUrl()).isEqualTo(UPDATED_PROVISIONING_URL);
        assertThat(testDevice.getNtpServer()).isEqualTo(UPDATED_NTP_SERVER);
        assertThat(testDevice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDevice.getConfiguration()).isEqualTo(DEFAULT_CONFIGURATION);
        assertThat(testDevice.getConfigurationContentType()).isEqualTo(DEFAULT_CONFIGURATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .mac(UPDATED_MAC)
            .inventoryNumber(UPDATED_INVENTORY_NUMBER)
            .location(UPDATED_LOCATION)
            .hostname(UPDATED_HOSTNAME)
            .webLogin(UPDATED_WEB_LOGIN)
            .webPassword(UPDATED_WEB_PASSWORD)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .subnetMask(UPDATED_SUBNET_MASK)
            .defaultGw(UPDATED_DEFAULT_GW)
            .dns1(UPDATED_DNS_1)
            .dns2(UPDATED_DNS_2)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL)
            .ntpServer(UPDATED_NTP_SERVER)
            .notes(UPDATED_NOTES)
            .configuration(UPDATED_CONFIGURATION)
            .configurationContentType(UPDATED_CONFIGURATION_CONTENT_TYPE);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getMac()).isEqualTo(UPDATED_MAC);
        assertThat(testDevice.getInventoryNumber()).isEqualTo(UPDATED_INVENTORY_NUMBER);
        assertThat(testDevice.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testDevice.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testDevice.getWebLogin()).isEqualTo(UPDATED_WEB_LOGIN);
        assertThat(testDevice.getWebPassword()).isEqualTo(UPDATED_WEB_PASSWORD);
        assertThat(testDevice.getDhcpEnabled()).isEqualTo(UPDATED_DHCP_ENABLED);
        assertThat(testDevice.getIpAddress()).isEqualTo(UPDATED_IP_ADDRESS);
        assertThat(testDevice.getSubnetMask()).isEqualTo(UPDATED_SUBNET_MASK);
        assertThat(testDevice.getDefaultGw()).isEqualTo(UPDATED_DEFAULT_GW);
        assertThat(testDevice.getDns1()).isEqualTo(UPDATED_DNS_1);
        assertThat(testDevice.getDns2()).isEqualTo(UPDATED_DNS_2);
        assertThat(testDevice.getProvisioningMode()).isEqualTo(UPDATED_PROVISIONING_MODE);
        assertThat(testDevice.getProvisioningUrl()).isEqualTo(UPDATED_PROVISIONING_URL);
        assertThat(testDevice.getNtpServer()).isEqualTo(UPDATED_NTP_SERVER);
        assertThat(testDevice.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testDevice.getConfiguration()).isEqualTo(UPDATED_CONFIGURATION);
        assertThat(testDevice.getConfigurationContentType()).isEqualTo(UPDATED_CONFIGURATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
