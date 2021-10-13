package ru.solodkov.voipadmin.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.domain.OtherDeviceType;
import ru.solodkov.voipadmin.repository.OtherDeviceTypeRepository;
import ru.solodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import ru.solodkov.voipadmin.service.mapper.OtherDeviceTypeMapper;

/**
 * Service Implementation for managing {@link OtherDeviceType}.
 */
@Service
@Transactional
public class OtherDeviceTypeService {

    private final Logger log = LoggerFactory.getLogger(OtherDeviceTypeService.class);

    private final OtherDeviceTypeRepository otherDeviceTypeRepository;

    private final OtherDeviceTypeMapper otherDeviceTypeMapper;

    public OtherDeviceTypeService(OtherDeviceTypeRepository otherDeviceTypeRepository, OtherDeviceTypeMapper otherDeviceTypeMapper) {
        this.otherDeviceTypeRepository = otherDeviceTypeRepository;
        this.otherDeviceTypeMapper = otherDeviceTypeMapper;
    }

    /**
     * Save a otherDeviceType.
     *
     * @param otherDeviceTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public OtherDeviceTypeDTO save(OtherDeviceTypeDTO otherDeviceTypeDTO) {
        log.debug("Request to save OtherDeviceType : {}", otherDeviceTypeDTO);
        OtherDeviceType otherDeviceType = otherDeviceTypeMapper.toEntity(otherDeviceTypeDTO);
        otherDeviceType = otherDeviceTypeRepository.save(otherDeviceType);
        return otherDeviceTypeMapper.toDto(otherDeviceType);
    }

    /**
     * Partially update a otherDeviceType.
     *
     * @param otherDeviceTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OtherDeviceTypeDTO> partialUpdate(OtherDeviceTypeDTO otherDeviceTypeDTO) {
        log.debug("Request to partially update OtherDeviceType : {}", otherDeviceTypeDTO);

        return otherDeviceTypeRepository
            .findById(otherDeviceTypeDTO.getId())
            .map(
                existingOtherDeviceType -> {
                    otherDeviceTypeMapper.partialUpdate(existingOtherDeviceType, otherDeviceTypeDTO);

                    return existingOtherDeviceType;
                }
            )
            .map(otherDeviceTypeRepository::save)
            .map(otherDeviceTypeMapper::toDto);
    }

    /**
     * Get all the otherDeviceTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OtherDeviceTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OtherDeviceTypes");
        return otherDeviceTypeRepository.findAll(pageable).map(otherDeviceTypeMapper::toDto);
    }

    /**
     * Get one otherDeviceType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OtherDeviceTypeDTO> findOne(Long id) {
        log.debug("Request to get OtherDeviceType : {}", id);
        return otherDeviceTypeRepository.findById(id).map(otherDeviceTypeMapper::toDto);
    }

    /**
     * Delete the otherDeviceType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OtherDeviceType : {}", id);
        otherDeviceTypeRepository.deleteById(id);
    }
}
