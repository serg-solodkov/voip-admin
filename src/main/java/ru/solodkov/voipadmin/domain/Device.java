package ru.solodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.solodkov.voipadmin.domain.enumeration.ProvisioningMode;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "mac", nullable = false)
    private String mac;

    @Column(name = "inventory_number")
    private String inventoryNumber;

    @Column(name = "location")
    private String location;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "web_login")
    private String webLogin;

    @Column(name = "web_password")
    private String webPassword;

    @Column(name = "dhcp_enabled")
    private Boolean dhcpEnabled;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "subnet_mask")
    private String subnetMask;

    @Column(name = "default_gw")
    private String defaultGw;

    @Column(name = "dns_1")
    private String dns1;

    @Column(name = "dns_2")
    private String dns2;

    @Enumerated(EnumType.STRING)
    @Column(name = "provisioning_mode")
    private ProvisioningMode provisioningMode;

    @Column(name = "provisioning_url")
    private String provisioningUrl;

    @Column(name = "ntp_server")
    private String ntpServer;

    @Column(name = "notes")
    private String notes;

    @Lob
    @Column(name = "configuration")
    private byte[] configuration;

    @Column(name = "configuration_content_type")
    private String configurationContentType;

    @OneToMany(mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "option", "selectedValues", "device" }, allowSetters = true)
    private Set<Setting> settings = new HashSet<>();

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "asteriskAccount", "device" }, allowSetters = true)
    private Set<VoipAccount> voipAccounts = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "settings", "voipAccounts", "children", "model", "responsiblePerson", "parent" }, allowSetters = true)
    private Set<Device> children = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "otherDeviceType", "vendor", "options" }, allowSetters = true)
    private DeviceModel model;

    @ManyToOne
    @JsonIgnoreProperties(value = { "department" }, allowSetters = true)
    private ResponsiblePerson responsiblePerson;

    @ManyToOne
    @JsonIgnoreProperties(value = { "settings", "voipAccounts", "children", "model", "responsiblePerson", "parent" }, allowSetters = true)
    private Device parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device id(Long id) {
        this.id = id;
        return this;
    }

    public String getMac() {
        return this.mac;
    }

    public Device mac(String mac) {
        this.mac = mac;
        return this;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getInventoryNumber() {
        return this.inventoryNumber;
    }

    public Device inventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
        return this;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getLocation() {
        return this.location;
    }

    public Device location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHostname() {
        return this.hostname;
    }

    public Device hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getWebLogin() {
        return this.webLogin;
    }

    public Device webLogin(String webLogin) {
        this.webLogin = webLogin;
        return this;
    }

    public void setWebLogin(String webLogin) {
        this.webLogin = webLogin;
    }

    public String getWebPassword() {
        return this.webPassword;
    }

    public Device webPassword(String webPassword) {
        this.webPassword = webPassword;
        return this;
    }

    public void setWebPassword(String webPassword) {
        this.webPassword = webPassword;
    }

    public Boolean getDhcpEnabled() {
        return this.dhcpEnabled;
    }

    public Device dhcpEnabled(Boolean dhcpEnabled) {
        this.dhcpEnabled = dhcpEnabled;
        return this;
    }

    public void setDhcpEnabled(Boolean dhcpEnabled) {
        this.dhcpEnabled = dhcpEnabled;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public Device ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubnetMask() {
        return this.subnetMask;
    }

    public Device subnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
        return this;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getDefaultGw() {
        return this.defaultGw;
    }

    public Device defaultGw(String defaultGw) {
        this.defaultGw = defaultGw;
        return this;
    }

    public void setDefaultGw(String defaultGw) {
        this.defaultGw = defaultGw;
    }

    public String getDns1() {
        return this.dns1;
    }

    public Device dns1(String dns1) {
        this.dns1 = dns1;
        return this;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return this.dns2;
    }

    public Device dns2(String dns2) {
        this.dns2 = dns2;
        return this;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public ProvisioningMode getProvisioningMode() {
        return this.provisioningMode;
    }

    public Device provisioningMode(ProvisioningMode provisioningMode) {
        this.provisioningMode = provisioningMode;
        return this;
    }

    public void setProvisioningMode(ProvisioningMode provisioningMode) {
        this.provisioningMode = provisioningMode;
    }

    public String getProvisioningUrl() {
        return this.provisioningUrl;
    }

    public Device provisioningUrl(String provisioningUrl) {
        this.provisioningUrl = provisioningUrl;
        return this;
    }

    public void setProvisioningUrl(String provisioningUrl) {
        this.provisioningUrl = provisioningUrl;
    }

    public String getNtpServer() {
        return this.ntpServer;
    }

    public Device ntpServer(String ntpServer) {
        this.ntpServer = ntpServer;
        return this;
    }

    public void setNtpServer(String ntpServer) {
        this.ntpServer = ntpServer;
    }

    public String getNotes() {
        return this.notes;
    }

    public Device notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public byte[] getConfiguration() {
        return this.configuration;
    }

    public Device configuration(byte[] configuration) {
        this.configuration = configuration;
        return this;
    }

    public void setConfiguration(byte[] configuration) {
        this.configuration = configuration;
    }

    public String getConfigurationContentType() {
        return this.configurationContentType;
    }

    public Device configurationContentType(String configurationContentType) {
        this.configurationContentType = configurationContentType;
        return this;
    }

    public void setConfigurationContentType(String configurationContentType) {
        this.configurationContentType = configurationContentType;
    }

    public Set<Setting> getSettings() {
        return this.settings;
    }

    public Device settings(Set<Setting> settings) {
        this.setSettings(settings);
        return this;
    }

    public Device addSettings(Setting setting) {
        this.settings.add(setting);
        setting.setDevice(this);
        return this;
    }

    public Device removeSettings(Setting setting) {
        this.settings.remove(setting);
        setting.setDevice(null);
        return this;
    }

    public void setSettings(Set<Setting> settings) {
        if (this.settings != null) {
            this.settings.forEach(i -> i.setDevice(null));
        }
        if (settings != null) {
            settings.forEach(i -> i.setDevice(this));
        }
        this.settings = settings;
    }

    public Set<VoipAccount> getVoipAccounts() {
        return this.voipAccounts;
    }

    public Device voipAccounts(Set<VoipAccount> voipAccounts) {
        this.setVoipAccounts(voipAccounts);
        return this;
    }

    public Device addVoipAccounts(VoipAccount voipAccount) {
        this.voipAccounts.add(voipAccount);
        voipAccount.setDevice(this);
        return this;
    }

    public Device removeVoipAccounts(VoipAccount voipAccount) {
        this.voipAccounts.remove(voipAccount);
        voipAccount.setDevice(null);
        return this;
    }

    public void setVoipAccounts(Set<VoipAccount> voipAccounts) {
        if (this.voipAccounts != null) {
            this.voipAccounts.forEach(i -> i.setDevice(null));
        }
        if (voipAccounts != null) {
            voipAccounts.forEach(i -> i.setDevice(this));
        }
        this.voipAccounts = voipAccounts;
    }

    public Set<Device> getChildren() {
        return this.children;
    }

    public Device children(Set<Device> devices) {
        this.setChildren(devices);
        return this;
    }

    public Device addChildren(Device device) {
        this.children.add(device);
        device.setParent(this);
        return this;
    }

    public Device removeChildren(Device device) {
        this.children.remove(device);
        device.setParent(null);
        return this;
    }

    public void setChildren(Set<Device> devices) {
        if (this.children != null) {
            this.children.forEach(i -> i.setParent(null));
        }
        if (devices != null) {
            devices.forEach(i -> i.setParent(this));
        }
        this.children = devices;
    }

    public DeviceModel getModel() {
        return this.model;
    }

    public Device model(DeviceModel deviceModel) {
        this.setModel(deviceModel);
        return this;
    }

    public void setModel(DeviceModel deviceModel) {
        this.model = deviceModel;
    }

    public ResponsiblePerson getResponsiblePerson() {
        return this.responsiblePerson;
    }

    public Device responsiblePerson(ResponsiblePerson responsiblePerson) {
        this.setResponsiblePerson(responsiblePerson);
        return this;
    }

    public void setResponsiblePerson(ResponsiblePerson responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public Device getParent() {
        return this.parent;
    }

    public Device parent(Device device) {
        this.setParent(device);
        return this;
    }

    public void setParent(Device device) {
        this.parent = device;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", mac='" + getMac() + "'" +
            ", inventoryNumber='" + getInventoryNumber() + "'" +
            ", location='" + getLocation() + "'" +
            ", hostname='" + getHostname() + "'" +
            ", webLogin='" + getWebLogin() + "'" +
            ", webPassword='" + getWebPassword() + "'" +
            ", dhcpEnabled='" + getDhcpEnabled() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", subnetMask='" + getSubnetMask() + "'" +
            ", defaultGw='" + getDefaultGw() + "'" +
            ", dns1='" + getDns1() + "'" +
            ", dns2='" + getDns2() + "'" +
            ", provisioningMode='" + getProvisioningMode() + "'" +
            ", provisioningUrl='" + getProvisioningUrl() + "'" +
            ", ntpServer='" + getNtpServer() + "'" +
            ", notes='" + getNotes() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", configurationContentType='" + getConfigurationContentType() + "'" +
            "}";
    }
}
