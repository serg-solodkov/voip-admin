package ru.solodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.solodkov.voipadmin.domain.enumeration.DeviceType;

/**
 * A DeviceModel.
 */
@Entity
@Table(name = "device_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeviceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "is_configurable", nullable = false)
    private Boolean isConfigurable;

    @Column(name = "lines_count")
    private Integer linesCount;

    @Lob
    @Column(name = "config_template")
    private byte[] configTemplate;

    @Column(name = "config_template_content_type")
    private String configTemplateContentType;

    @Lob
    @Column(name = "firmware_file")
    private byte[] firmwareFile;

    @Column(name = "firmware_file_content_type")
    private String firmwareFileContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    private DeviceType deviceType;

    @ManyToOne
    private OtherDeviceType otherDeviceType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "options" }, allowSetters = true)
    private Vendor vendor;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_device_model__options",
        joinColumns = @JoinColumn(name = "device_model_id"),
        inverseJoinColumns = @JoinColumn(name = "options_id")
    )
    @JsonIgnoreProperties(value = { "possibleValues", "vendors", "models" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeviceModel id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public DeviceModel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsConfigurable() {
        return this.isConfigurable;
    }

    public DeviceModel isConfigurable(Boolean isConfigurable) {
        this.isConfigurable = isConfigurable;
        return this;
    }

    public void setIsConfigurable(Boolean isConfigurable) {
        this.isConfigurable = isConfigurable;
    }

    public Integer getLinesCount() {
        return this.linesCount;
    }

    public DeviceModel linesCount(Integer linesCount) {
        this.linesCount = linesCount;
        return this;
    }

    public void setLinesCount(Integer linesCount) {
        this.linesCount = linesCount;
    }

    public byte[] getConfigTemplate() {
        return this.configTemplate;
    }

    public DeviceModel configTemplate(byte[] configTemplate) {
        this.configTemplate = configTemplate;
        return this;
    }

    public void setConfigTemplate(byte[] configTemplate) {
        this.configTemplate = configTemplate;
    }

    public String getConfigTemplateContentType() {
        return this.configTemplateContentType;
    }

    public DeviceModel configTemplateContentType(String configTemplateContentType) {
        this.configTemplateContentType = configTemplateContentType;
        return this;
    }

    public void setConfigTemplateContentType(String configTemplateContentType) {
        this.configTemplateContentType = configTemplateContentType;
    }

    public byte[] getFirmwareFile() {
        return this.firmwareFile;
    }

    public DeviceModel firmwareFile(byte[] firmwareFile) {
        this.firmwareFile = firmwareFile;
        return this;
    }

    public void setFirmwareFile(byte[] firmwareFile) {
        this.firmwareFile = firmwareFile;
    }

    public String getFirmwareFileContentType() {
        return this.firmwareFileContentType;
    }

    public DeviceModel firmwareFileContentType(String firmwareFileContentType) {
        this.firmwareFileContentType = firmwareFileContentType;
        return this;
    }

    public void setFirmwareFileContentType(String firmwareFileContentType) {
        this.firmwareFileContentType = firmwareFileContentType;
    }

    public DeviceType getDeviceType() {
        return this.deviceType;
    }

    public DeviceModel deviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public OtherDeviceType getOtherDeviceType() {
        return this.otherDeviceType;
    }

    public DeviceModel otherDeviceType(OtherDeviceType otherDeviceType) {
        this.setOtherDeviceType(otherDeviceType);
        return this;
    }

    public void setOtherDeviceType(OtherDeviceType otherDeviceType) {
        this.otherDeviceType = otherDeviceType;
    }

    public Vendor getVendor() {
        return this.vendor;
    }

    public DeviceModel vendor(Vendor vendor) {
        this.setVendor(vendor);
        return this;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public DeviceModel options(Set<Option> options) {
        this.setOptions(options);
        return this;
    }

    public DeviceModel addOptions(Option option) {
        this.options.add(option);
        option.getModels().add(this);
        return this;
    }

    public DeviceModel removeOptions(Option option) {
        this.options.remove(option);
        option.getModels().remove(this);
        return this;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceModel)) {
            return false;
        }
        return id != null && id.equals(((DeviceModel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceModel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isConfigurable='" + getIsConfigurable() + "'" +
            ", linesCount=" + getLinesCount() +
            ", configTemplate='" + getConfigTemplate() + "'" +
            ", configTemplateContentType='" + getConfigTemplateContentType() + "'" +
            ", firmwareFile='" + getFirmwareFile() + "'" +
            ", firmwareFileContentType='" + getFirmwareFileContentType() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            "}";
    }
}
