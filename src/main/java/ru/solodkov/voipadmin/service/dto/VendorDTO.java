package ru.solodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ru.solodkov.voipadmin.domain.Vendor} entity.
 */
public class VendorDTO implements Serializable {

    private Long id;

    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VendorDTO)) {
            return false;
        }

        VendorDTO vendorDTO = (VendorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vendorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VendorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
