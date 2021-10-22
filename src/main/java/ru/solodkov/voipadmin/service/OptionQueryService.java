package ru.solodkov.voipadmin.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.domain.*; // for static metamodels
import ru.solodkov.voipadmin.domain.Option;
import ru.solodkov.voipadmin.repository.OptionRepository;
import ru.solodkov.voipadmin.service.criteria.OptionCriteria;
import ru.solodkov.voipadmin.service.dto.OptionDTO;
import ru.solodkov.voipadmin.service.mapper.OptionMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Option} entities in the database.
 * The main input is a {@link OptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OptionDTO} or a {@link Page} of {@link OptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OptionQueryService extends QueryService<Option> {

    private final Logger log = LoggerFactory.getLogger(OptionQueryService.class);

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    public OptionQueryService(OptionRepository optionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
    }

    /**
     * Return a {@link List} of {@link OptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OptionDTO> findByCriteria(OptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Option> specification = createSpecification(criteria);
        return optionMapper.toDto(optionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionDTO> findByCriteria(OptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Option> specification = createSpecification(criteria);
        return optionRepository.findAll(specification, page).map(optionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Option> specification = createSpecification(criteria);
        return optionRepository.count(specification);
    }

    /**
     * Function to convert {@link OptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Option> createSpecification(OptionCriteria criteria) {
        Specification<Option> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Option_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Option_.code));
            }
            if (criteria.getDescr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescr(), Option_.descr));
            }
            if (criteria.getValueType() != null) {
                specification = specification.and(buildSpecification(criteria.getValueType(), Option_.valueType));
            }
            if (criteria.getMultiple() != null) {
                specification = specification.and(buildSpecification(criteria.getMultiple(), Option_.multiple));
            }
            if (criteria.getPossibleValuesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPossibleValuesId(),
                            root -> root.join(Option_.possibleValues, JoinType.LEFT).get(OptionValue_.id)
                        )
                    );
            }
            if (criteria.getVendorsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getVendorsId(), root -> root.join(Option_.vendors, JoinType.LEFT).get(Vendor_.id))
                    );
            }
            if (criteria.getModelsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getModelsId(), root -> root.join(Option_.models, JoinType.LEFT).get(DeviceModel_.id))
                    );
            }
        }
        return specification;
    }
}
