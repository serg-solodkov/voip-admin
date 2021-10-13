package ru.solodkov.voipadmin.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.domain.ResponsiblePerson;
import ru.solodkov.voipadmin.repository.ResponsiblePersonRepository;
import ru.solodkov.voipadmin.service.dto.ResponsiblePersonDTO;
import ru.solodkov.voipadmin.service.mapper.ResponsiblePersonMapper;

/**
 * Service Implementation for managing {@link ResponsiblePerson}.
 */
@Service
@Transactional
public class ResponsiblePersonService {

    private final Logger log = LoggerFactory.getLogger(ResponsiblePersonService.class);

    private final ResponsiblePersonRepository responsiblePersonRepository;

    private final ResponsiblePersonMapper responsiblePersonMapper;

    public ResponsiblePersonService(
        ResponsiblePersonRepository responsiblePersonRepository,
        ResponsiblePersonMapper responsiblePersonMapper
    ) {
        this.responsiblePersonRepository = responsiblePersonRepository;
        this.responsiblePersonMapper = responsiblePersonMapper;
    }

    /**
     * Save a responsiblePerson.
     *
     * @param responsiblePersonDTO the entity to save.
     * @return the persisted entity.
     */
    public ResponsiblePersonDTO save(ResponsiblePersonDTO responsiblePersonDTO) {
        log.debug("Request to save ResponsiblePerson : {}", responsiblePersonDTO);
        ResponsiblePerson responsiblePerson = responsiblePersonMapper.toEntity(responsiblePersonDTO);
        responsiblePerson = responsiblePersonRepository.save(responsiblePerson);
        return responsiblePersonMapper.toDto(responsiblePerson);
    }

    /**
     * Partially update a responsiblePerson.
     *
     * @param responsiblePersonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ResponsiblePersonDTO> partialUpdate(ResponsiblePersonDTO responsiblePersonDTO) {
        log.debug("Request to partially update ResponsiblePerson : {}", responsiblePersonDTO);

        return responsiblePersonRepository
            .findById(responsiblePersonDTO.getId())
            .map(
                existingResponsiblePerson -> {
                    responsiblePersonMapper.partialUpdate(existingResponsiblePerson, responsiblePersonDTO);

                    return existingResponsiblePerson;
                }
            )
            .map(responsiblePersonRepository::save)
            .map(responsiblePersonMapper::toDto);
    }

    /**
     * Get all the responsiblePeople.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ResponsiblePersonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ResponsiblePeople");
        return responsiblePersonRepository.findAll(pageable).map(responsiblePersonMapper::toDto);
    }

    /**
     * Get one responsiblePerson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResponsiblePersonDTO> findOne(Long id) {
        log.debug("Request to get ResponsiblePerson : {}", id);
        return responsiblePersonRepository.findById(id).map(responsiblePersonMapper::toDto);
    }

    /**
     * Delete the responsiblePerson by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResponsiblePerson : {}", id);
        responsiblePersonRepository.deleteById(id);
    }
}
