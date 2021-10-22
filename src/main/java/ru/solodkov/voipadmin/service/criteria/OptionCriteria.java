package ru.solodkov.voipadmin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import ru.solodkov.voipadmin.domain.enumeration.OptionValueType;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ru.solodkov.voipadmin.domain.Option} entity. This class is used
 * in {@link ru.solodkov.voipadmin.web.rest.OptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OptionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OptionValueType
     */
    public static class OptionValueTypeFilter extends Filter<OptionValueType> {

        public OptionValueTypeFilter() {}

        public OptionValueTypeFilter(OptionValueTypeFilter filter) {
            super(filter);
        }

        @Override
        public OptionValueTypeFilter copy() {
            return new OptionValueTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter descr;

    private OptionValueTypeFilter valueType;

    private BooleanFilter multiple;

    private LongFilter possibleValuesId;

    private LongFilter vendorsId;

    private LongFilter modelsId;

    public OptionCriteria() {}

    public OptionCriteria(OptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.descr = other.descr == null ? null : other.descr.copy();
        this.valueType = other.valueType == null ? null : other.valueType.copy();
        this.multiple = other.multiple == null ? null : other.multiple.copy();
        this.possibleValuesId = other.possibleValuesId == null ? null : other.possibleValuesId.copy();
        this.vendorsId = other.vendorsId == null ? null : other.vendorsId.copy();
        this.modelsId = other.modelsId == null ? null : other.modelsId.copy();
    }

    @Override
    public OptionCriteria copy() {
        return new OptionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDescr() {
        return descr;
    }

    public StringFilter descr() {
        if (descr == null) {
            descr = new StringFilter();
        }
        return descr;
    }

    public void setDescr(StringFilter descr) {
        this.descr = descr;
    }

    public OptionValueTypeFilter getValueType() {
        return valueType;
    }

    public OptionValueTypeFilter valueType() {
        if (valueType == null) {
            valueType = new OptionValueTypeFilter();
        }
        return valueType;
    }

    public void setValueType(OptionValueTypeFilter valueType) {
        this.valueType = valueType;
    }

    public BooleanFilter getMultiple() {
        return multiple;
    }

    public BooleanFilter multiple() {
        if (multiple == null) {
            multiple = new BooleanFilter();
        }
        return multiple;
    }

    public void setMultiple(BooleanFilter multiple) {
        this.multiple = multiple;
    }

    public LongFilter getPossibleValuesId() {
        return possibleValuesId;
    }

    public LongFilter possibleValuesId() {
        if (possibleValuesId == null) {
            possibleValuesId = new LongFilter();
        }
        return possibleValuesId;
    }

    public void setPossibleValuesId(LongFilter possibleValuesId) {
        this.possibleValuesId = possibleValuesId;
    }

    public LongFilter getVendorsId() {
        return vendorsId;
    }

    public LongFilter vendorsId() {
        if (vendorsId == null) {
            vendorsId = new LongFilter();
        }
        return vendorsId;
    }

    public void setVendorsId(LongFilter vendorsId) {
        this.vendorsId = vendorsId;
    }

    public LongFilter getModelsId() {
        return modelsId;
    }

    public LongFilter modelsId() {
        if (modelsId == null) {
            modelsId = new LongFilter();
        }
        return modelsId;
    }

    public void setModelsId(LongFilter modelsId) {
        this.modelsId = modelsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OptionCriteria that = (OptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(descr, that.descr) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(multiple, that.multiple) &&
            Objects.equals(possibleValuesId, that.possibleValuesId) &&
            Objects.equals(vendorsId, that.vendorsId) &&
            Objects.equals(modelsId, that.modelsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, descr, valueType, multiple, possibleValuesId, vendorsId, modelsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (descr != null ? "descr=" + descr + ", " : "") +
            (valueType != null ? "valueType=" + valueType + ", " : "") +
            (multiple != null ? "multiple=" + multiple + ", " : "") +
            (possibleValuesId != null ? "possibleValuesId=" + possibleValuesId + ", " : "") +
            (vendorsId != null ? "vendorsId=" + vendorsId + ", " : "") +
            (modelsId != null ? "modelsId=" + modelsId + ", " : "") +
            "}";
    }
}
