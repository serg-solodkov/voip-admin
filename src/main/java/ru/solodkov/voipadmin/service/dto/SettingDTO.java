package ru.solodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ru.solodkov.voipadmin.domain.Setting} entity.
 */
public class SettingDTO implements Serializable {

    private Long id;

    private String textValue;

    private OptionDTO option;

    private Set<OptionValueDTO> selectedValues = new HashSet<>();

    private DeviceDTO device;

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

    public void setSelectedValues(Set<OptionValueDTO> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SettingDTO)) {
            return false;
        }

        SettingDTO settingDTO = (SettingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, settingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SettingDTO{" +
            "id=" + getId() +
            ", textValue='" + getTextValue() + "'" +
            ", option=" + getOption() +
            ", selectedValues=" + getSelectedValues() +
            ", device=" + getDevice() +
            "}";
    }
}
