package ru.solodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Setting.
 */
@Entity
@Table(name = "setting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "text_value")
    private String textValue;

    @ManyToOne
    @JsonIgnoreProperties(value = { "possibleValues", "vendors", "models" }, allowSetters = true)
    private Option option;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_setting__selected_values",
        joinColumns = @JoinColumn(name = "setting_id"),
        inverseJoinColumns = @JoinColumn(name = "selected_values_id")
    )
    @JsonIgnoreProperties(value = { "option", "settings" }, allowSetters = true)
    private Set<OptionValue> selectedValues = new HashSet<>();

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

    public Setting id(Long id) {
        this.id = id;
        return this;
    }

    public String getTextValue() {
        return this.textValue;
    }

    public Setting textValue(String textValue) {
        this.textValue = textValue;
        return this;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public Option getOption() {
        return this.option;
    }

    public Setting option(Option option) {
        this.setOption(option);
        return this;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public Set<OptionValue> getSelectedValues() {
        return this.selectedValues;
    }

    public Setting selectedValues(Set<OptionValue> optionValues) {
        this.setSelectedValues(optionValues);
        return this;
    }

    public Setting addSelectedValues(OptionValue optionValue) {
        this.selectedValues.add(optionValue);
        optionValue.getSettings().add(this);
        return this;
    }

    public Setting removeSelectedValues(OptionValue optionValue) {
        this.selectedValues.remove(optionValue);
        optionValue.getSettings().remove(this);
        return this;
    }

    public void setSelectedValues(Set<OptionValue> optionValues) {
        this.selectedValues = optionValues;
    }

    public Device getDevice() {
        return this.device;
    }

    public Setting device(Device device) {
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
        if (!(o instanceof Setting)) {
            return false;
        }
        return id != null && id.equals(((Setting) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Setting{" +
            "id=" + getId() +
            ", textValue='" + getTextValue() + "'" +
            "}";
    }
}
