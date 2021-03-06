package com.voipadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.voipadmin.domain.enumeration.OptionValueType;

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
    private Set<OptionValue> possibleValues = new HashSet<>();

    @ManyToMany(mappedBy = "options")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<DeviceModel> models = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "vendor_option",
        joinColumns = @JoinColumn(name = "option_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "vendor_id", referencedColumnName = "id"))
    private Set<Vendor> vendors;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Option code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescr() {
        return descr;
    }

    public Option descr(String descr) {
        this.descr = descr;
        return this;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public OptionValueType getValueType() {
        return valueType;
    }

    public Option valueType(OptionValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(OptionValueType valueType) {
        this.valueType = valueType;
    }

    public Boolean isMultiple() {
        return multiple;
    }

    public Option multiple(Boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Set<OptionValue> getPossibleValues() {
        return possibleValues;
    }

    public Option possibleValues(Set<OptionValue> optionValues) {
        this.possibleValues = optionValues;
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
        this.possibleValues = optionValues;
    }

    public Set<DeviceModel> getModels() {
        return models;
    }

    public Option models(Set<DeviceModel> deviceModels) {
        this.models = deviceModels;
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
        this.models = deviceModels;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public Set<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(Set<Vendor> vendors) {
        this.vendors = vendors;
    }

    public Option addVendor(Vendor vendor) {
        this.vendors.add(vendor);
        vendor.getOptions().add(this);
        return this;
    }

    public Option removeModels(Vendor vendor) {
        this.vendors.remove(vendor);
        vendor.getOptions().remove(this);
        return this;
    }

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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Option{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", descr='" + getDescr() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", multiple='" + isMultiple() + "'" +
            "}";
    }
}
