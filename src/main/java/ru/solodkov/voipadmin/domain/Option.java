package ru.solodkov.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.solodkov.voipadmin.domain.enumeration.OptionValueType;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "descr")
    private String descr;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    private OptionValueType valueType;

    @Column(name = "multiple")
    private Boolean multiple;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "option", "settings" }, allowSetters = true)
    private Set<OptionValue> possibleValues = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_option__vendors",
        joinColumns = @JoinColumn(name = "option_id"),
        inverseJoinColumns = @JoinColumn(name = "vendors_id")
    )
    @JsonIgnoreProperties(value = { "options" }, allowSetters = true)
    private Set<Vendor> vendors = new HashSet<>();

    @ManyToMany(mappedBy = "options")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "otherDeviceType", "vendor", "options" }, allowSetters = true)
    private Set<DeviceModel> models = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Option id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Option code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescr() {
        return this.descr;
    }

    public Option descr(String descr) {
        this.descr = descr;
        return this;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public OptionValueType getValueType() {
        return this.valueType;
    }

    public Option valueType(OptionValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(OptionValueType valueType) {
        this.valueType = valueType;
    }

    public Boolean getMultiple() {
        return this.multiple;
    }

    public Option multiple(Boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Set<OptionValue> getPossibleValues() {
        return this.possibleValues;
    }

    public Option possibleValues(Set<OptionValue> optionValues) {
        this.setPossibleValues(optionValues);
        return this;
    }

    public Option addPossibleValues(OptionValue optionValue) {
        this.possibleValues.add(optionValue);
        optionValue.setOption(this);
        return this;
    }

    public Option removePossibleValues(OptionValue optionValue) {
        this.possibleValues.remove(optionValue);
        optionValue.setOption(null);
        return this;
    }

    public void setPossibleValues(Set<OptionValue> optionValues) {
        if (this.possibleValues != null) {
            this.possibleValues.forEach(i -> i.setOption(null));
        }
        if (optionValues != null) {
            optionValues.forEach(i -> i.setOption(this));
        }
        this.possibleValues = optionValues;
    }

    public Set<Vendor> getVendors() {
        return this.vendors;
    }

    public Option vendors(Set<Vendor> vendors) {
        this.setVendors(vendors);
        return this;
    }

    public Option addVendors(Vendor vendor) {
        this.vendors.add(vendor);
        vendor.getOptions().add(this);
        return this;
    }

    public Option removeVendors(Vendor vendor) {
        this.vendors.remove(vendor);
        vendor.getOptions().remove(this);
        return this;
    }

    public void setVendors(Set<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Set<DeviceModel> getModels() {
        return this.models;
    }

    public Option models(Set<DeviceModel> deviceModels) {
        this.setModels(deviceModels);
        return this;
    }

    public Option addModels(DeviceModel deviceModel) {
        this.models.add(deviceModel);
        deviceModel.getOptions().add(this);
        return this;
    }

    public Option removeModels(DeviceModel deviceModel) {
        this.models.remove(deviceModel);
        deviceModel.getOptions().remove(this);
        return this;
    }

    public void setModels(Set<DeviceModel> deviceModels) {
        if (this.models != null) {
            this.models.forEach(i -> i.removeOptions(this));
        }
        if (deviceModels != null) {
            deviceModels.forEach(i -> i.addOptions(this));
        }
        this.models = deviceModels;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        return id != null && id.equals(((Option) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", descr='" + getDescr() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", multiple='" + getMultiple() + "'" +
            "}";
    }
}
