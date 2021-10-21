package ru.solodkov.voipadmin.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ru.solodkov.voipadmin.domain.VoipAccount} entity.
 */
public class VoipAccountDTO implements Serializable {

    private Long id;

    private Boolean manuallyCreated;

    @NotNull
    private String username;

    private String password;

    private String sipServer;

    private String sipPort;

    private Boolean lineEnable;

    private Integer lineNumber;

    private AsteriskAccountDTO asteriskAccount;

    private DeviceDTO device;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getManuallyCreated() {
        return manuallyCreated;
    }

    public void setManuallyCreated(Boolean manuallyCreated) {
        this.manuallyCreated = manuallyCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSipServer() {
        return sipServer;
    }

    public void setSipServer(String sipServer) {
        this.sipServer = sipServer;
    }

    public String getSipPort() {
        return sipPort;
    }

    public void setSipPort(String sipPort) {
        this.sipPort = sipPort;
    }

    public Boolean getLineEnable() {
        return lineEnable;
    }

    public void setLineEnable(Boolean lineEnable) {
        this.lineEnable = lineEnable;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public AsteriskAccountDTO getAsteriskAccount() {
        return asteriskAccount;
    }

    public void setAsteriskAccount(AsteriskAccountDTO asteriskAccount) {
        this.asteriskAccount = asteriskAccount;
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
        if (!(o instanceof VoipAccountDTO)) {
            return false;
        }

        VoipAccountDTO voipAccountDTO = (VoipAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, voipAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoipAccountDTO{" +
            "id=" + getId() +
            ", manuallyCreated='" + getManuallyCreated() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", sipServer='" + getSipServer() + "'" +
            ", sipPort='" + getSipPort() + "'" +
            ", lineEnable='" + getLineEnable() + "'" +
            ", lineNumber='" + getLineNumber() + "'" +
            ", asteriskAccount=" + getAsteriskAccount() +
            ", device=" + getDevice() +
            "}";
    }
}
