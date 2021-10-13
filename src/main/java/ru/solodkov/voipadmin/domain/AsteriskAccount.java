package ru.solodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AsteriskAccount.
 */
@Entity
@Table(name = "asterisk_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AsteriskAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "asterisk_id")
    private String asteriskId;

    @JsonIgnoreProperties(value = { "asteriskAccount", "device" }, allowSetters = true)
    @OneToOne(mappedBy = "asteriskAccount")
    private VoipAccount voipAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AsteriskAccount id(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public AsteriskAccount username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAsteriskId() {
        return this.asteriskId;
    }

    public AsteriskAccount asteriskId(String asteriskId) {
        this.asteriskId = asteriskId;
        return this;
    }

    public void setAsteriskId(String asteriskId) {
        this.asteriskId = asteriskId;
    }

    public VoipAccount getVoipAccount() {
        return this.voipAccount;
    }

    public AsteriskAccount voipAccount(VoipAccount voipAccount) {
        this.setVoipAccount(voipAccount);
        return this;
    }

    public void setVoipAccount(VoipAccount voipAccount) {
        if (this.voipAccount != null) {
            this.voipAccount.setAsteriskAccount(null);
        }
        if (voipAccount != null) {
            voipAccount.setAsteriskAccount(this);
        }
        this.voipAccount = voipAccount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsteriskAccount)) {
            return false;
        }
        return id != null && id.equals(((AsteriskAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsteriskAccount{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", asteriskId='" + getAsteriskId() + "'" +
            "}";
    }
}
