package com.voipadmin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DTO for the {@link com.voipadmin.domain.Setting} entity.
 */
public class SettingDTO implements Serializable {

    private Long id;

    private String textValue;

    private OptionDTO option;

    private Set<OptionValueDTO> selectedValues = new HashSet<>();

    private Long deviceId;

    private String deviceMac;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public OptionDTO getOption() {
        return option;
    }

    public void setOption(OptionDTO option) {
        this.option = option;
    }

    public Set<OptionValueDTO> getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(Set<OptionValueDTO> optionValues) {
        this.selectedValues = optionValues;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettingDTO)) {
            return false;
        }

        return id != null && id.equals(((SettingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettingDTO{" +
            "id=" + getId() +
            ", textValue='" + getTextValue() + "'" +
            ", selectedValues='" + getSelectedValues() + "'" +
            ", deviceId=" + getDeviceId() +
            ", deviceMac='" + getDeviceMac() + "'" +
            "}";
    }
}
