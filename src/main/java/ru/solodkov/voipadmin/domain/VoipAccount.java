package ru.solodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VoipAccount.
 */
@Entity
@Table(name = "voip_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VoipAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "manually_created")
    private Boolean manuallyCreated;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "sip_server")
    private String sipServer;

    @Column(name = "sip_port")
    private String sipPort;

    @Column(name = "line_enable")
    private Boolean lineEnable;

    @Column(name = "line_number")
    private Integer lineNumber;

    @JsonIgnoreProperties(value = { "voipAccount" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private AsteriskAccount asteriskAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "settings", "voipAccounts", "children", "model", "responsiblePerson", "parent" }, allowSetters = true)
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VoipAccount id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getManuallyCreated() {
        return this.manuallyCreated;
    }

    public VoipAccount manuallyCreated(Boolean manuallyCreated) {
        this.manuallyCreated = manuallyCreated;
        return this;
    }

    public void setManuallyCreated(Boolean manuallyCreated) {
        this.manuallyCreated = manuallyCreated;
    }

    public String getUsername() {
        return this.username;
    }

    public VoipAccount username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public VoipAccount password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSipServer() {
        return this.sipServer;
    }

    public VoipAccount sipServer(String sipServer) {
        this.sipServer = sipServer;
        return this;
    }

    public void setSipServer(String sipServer) {
        this.sipServer = sipServer;
    }

    public String getSipPort() {
        return this.sipPort;
    }

    public VoipAccount sipPort(String sipPort) {
        this.sipPort = sipPort;
        return this;
    }

    public void setSipPort(String sipPort) {
        this.sipPort = sipPort;
    }

    public Boolean getLineEnable() {
        return this.lineEnable;
    }

    public VoipAccount lineEnable(Boolean lineEnable) {
        this.lineEnable = lineEnable;
        return this;
    }

    public void setLineEnable(Boolean lineEnable) {
        this.lineEnable = lineEnable;
    }

    public Integer getLineNumber() {
        return this.lineNumber;
    }

    public VoipAccount lineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public AsteriskAccount getAsteriskAccount() {
        return this.asteriskAccount;
    }

    public VoipAccount asteriskAccount(AsteriskAccount asteriskAccount) {
        this.setAsteriskAccount(asteriskAccount);
        return this;
    }

    public void setAsteriskAccount(AsteriskAccount asteriskAccount) {
        this.asteriskAccount = asteriskAccount;
    }

    public Device getDevice() {
        return this.device;
    }

    public VoipAccount device(Device device) {
        this.setDevice(device);
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoipAccount)) {
            return false;
        }
        return id != null && id.equals(((VoipAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VoipAccount{" +
            "id=" + getId() +
            ", manuallyCreated='" + getManuallyCreated() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", sipServer='" + getSipServer() + "'" +
            ", sipPort='" + getSipPort() + "'" +
            ", lineEnable='" + getLineEnable() + "'" +
            ", lineNumber='" + getLineNumber() + "'" +
            "}";
    }
}
