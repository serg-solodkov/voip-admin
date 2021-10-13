package ru.solodkov.voipadmin.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.domain.OptionValue;
import ru.solodkov.voipadmin.repository.OptionValueRepository;
import ru.solodkov.voipadmin.service.dto.OptionValueDTO;
import ru.solodkov.voipadmin.service.mapper.OptionValueMapper;

/**
 * Service Implementation for managing {@link OptionValue}.
 */
@Service
@Transactional
public class OptionValueService {

    private final Logger log = LoggerFactory.getLogger(OptionValueService.class);

    private final OptionValueRepository optionValueRepository;

    private final OptionValueMapper optionValueMapper;

    public OptionValueService(OptionValueRepository optionValueRepository, OptionValueMapper optionValueMapper) {
        this.optionValueRepository = optionValueRepository;
        this.optionValueMapper = optionValueMapper;
    }

    /**
     * Save a optionValue.
     *
     * @param optionValueDTO the entity to save.
     * @return the persisted entity.
     */
    public OptionValueDTO save(OptionValueDTO optionValueDTO) {
        log.debug("Request to save OptionValue : {}", optionValueDTO);
        OptionValue optionValue = optionValueMapper.toEntity(optionValueDTO);
        optionValue = optionValueRepository.save(optionValue);
        return optionValueMapper.toDto(optionValue);
    }

    /**
     * Partially update a optionValue.
     *
     * @param optionValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OptionValueDTO> partialUpdate(OptionValueDTO optionValueDTO) {
        log.debug("Request to partially update OptionValue : {}", optionValueDTO);

        return optionValueRepository
            .findById(optionValueDTO.getId())
            .map(
                existingOptionValue -> {
                    optionValueMapper.partialUpdate(existingOptionValue, optionValueDTO);

                    return existingOptionValue;
                }
            )
            .map(optionValueRepository::save)
            .map(optionValueMapper::toDto);
    }

    /**
     * Get all the optionValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OptionValues");
        return optionValueRepository.findAll(pageable).map(optionValueMapper::toDto);
    }

    /**
     * Get one optionValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OptionValueDTO> findOne(Long id) {
        log.debug("Request to get OptionValue : {}", id);
        return optionValueRepository.findById(id).map(optionValueMapper::toDto);
    }

    /**
     * Delete the optionValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OptionValue : {}", id);
        optionValueRepository.deleteById(id);
    }
}
