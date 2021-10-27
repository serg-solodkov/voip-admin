package ru.solodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import ru.solodkov.voipadmin.domain.enumeration.OptionValueType;

/**
 * A DTO for the {@link ru.solodkov.voipadmin.domain.Option} entity.
 */
public class OptionDTO implements Serializable {

    private Long id;

    private String code;

    private String descr;

    private String codeWithDescr;

    private OptionValueType valueType;

    private Boolean multiple;

    private Set<VendorDTO> vendors = new HashSet<>();

    private Set<OptionValueDTO> possibleValues = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getCodeWithDescr() {
        return codeWithDescr;
    }

    public void setCodeWithDescr(String codeWithDescr) {
        this.codeWithDescr = codeWithDescr;
    }

    public OptionValueType getValueType() {
        return valueType;
    }

    public void setValueType(OptionValueType valueType) {
        this.valueType = valueType;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Set<VendorDTO> getVendors() {
        return vendors;
    }

    public void setVendors(Set<VendorDTO> vendors) {
        this.vendors = vendors;
    }

    public Set<OptionValueDTO> getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(Set<OptionValueDTO> possibleValues) {
        this.possibleValues = possibleValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionDTO)) {
            return false;
        }

        OptionDTO optionDTO = (OptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, optionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", descr='" + getDescr() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", multiple='" + getMultiple() + "'" +
            ", vendors=" + getVendors() +
            "}";
    }
}
