package ru.solodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ru.solodkov.voipadmin.domain.AsteriskAccount} entity.
 */
public class AsteriskAccountDTO implements Serializable {

    private Long id;

    private String username;

    private String asteriskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAsteriskId() {
        return asteriskId;
    }

    public void setAsteriskId(String asteriskId) {
        this.asteriskId = asteriskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsteriskAccountDTO)) {
            return false;
        }

        AsteriskAccountDTO asteriskAccountDTO = (AsteriskAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, asteriskAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsteriskAccountDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", asteriskId='" + getAsteriskId() + "'" +
            "}";
    }
}
