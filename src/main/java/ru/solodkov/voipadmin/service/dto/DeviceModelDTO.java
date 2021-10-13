package ru.solodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;
import ru.solodkov.voipadmin.domain.enumeration.DeviceType;

/**
 * A DTO for the {@link ru.solodkov.voipadmin.domain.DeviceModel} entity.
 */
public class DeviceModelDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Boolean isConfigurable;

    private Integer linesCount;

    @Lob
    private byte[] configTemplate;

    private String configTemplateContentType;

    @Lob
    private byte[] firmwareFile;

    private String firmwareFileContentType;
    private DeviceType deviceType;

    private OtherDeviceTypeDTO otherDeviceType;

    private VendorDTO vendor;

    private Set<OptionDTO> options = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsConfigurable() {
        return isConfigurable;
    }

    public void setIsConfigurable(Boolean isConfigurable) {
        this.isConfigurable = isConfigurable;
    }

    public Integer getLinesCount() {
        return linesCount;
    }

    public void setLinesCount(Integer linesCount) {
        this.linesCount = linesCount;
    }

    public byte[] getConfigTemplate() {
        return configTemplate;
    }

    public void setConfigTemplate(byte[] configTemplate) {
        this.configTemplate = configTemplate;
    }

    public String getConfigTemplateContentType() {
        return configTemplateContentType;
    }

    public void setConfigTemplateContentType(String configTemplateContentType) {
        this.configTemplateContentType = configTemplateContentType;
    }

    public byte[] getFirmwareFile() {
        return firmwareFile;
    }

    public void setFirmwareFile(byte[] firmwareFile) {
        this.firmwareFile = firmwareFile;
    }

    public String getFirmwareFileContentType() {
        return firmwareFileContentType;
    }

    public void setFirmwareFileContentType(String firmwareFileContentType) {
        this.firmwareFileContentType = firmwareFileContentType;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public OtherDeviceTypeDTO getOtherDeviceType() {
        return otherDeviceType;
    }

    public void setOtherDeviceType(OtherDeviceTypeDTO otherDeviceType) {
        this.otherDeviceType = otherDeviceType;
    }

    public VendorDTO getVendor() {
        return vendor;
    }

    public void setVendor(VendorDTO vendor) {
        this.vendor = vendor;
    }

    public Set<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(Set<OptionDTO> options) {
        this.options = options;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceModelDTO)) {
            return false;
        }

        DeviceModelDTO deviceModelDTO = (DeviceModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceModelDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isConfigurable='" + getIsConfigurable() + "'" +
            ", linesCount=" + getLinesCount() +
            ", configTemplate='" + getConfigTemplate() + "'" +
            ", firmwareFile='" + getFirmwareFile() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", otherDeviceType=" + getOtherDeviceType() +
            ", vendor=" + getVendor() +
            ", options=" + getOptions() +
            "}";
    }
}
