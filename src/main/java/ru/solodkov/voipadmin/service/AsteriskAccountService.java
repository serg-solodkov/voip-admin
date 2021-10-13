package ru.solodkov.voipadmin.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.domain.AsteriskAccount;
import ru.solodkov.voipadmin.repository.AsteriskAccountRepository;
import ru.solodkov.voipadmin.service.dto.AsteriskAccountDTO;
import ru.solodkov.voipadmin.service.mapper.AsteriskAccountMapper;

/**
 * Service Implementation for managing {@link AsteriskAccount}.
 */
@Service
@Transactional
public class AsteriskAccountService {

    private final Logger log = LoggerFactory.getLogger(AsteriskAccountService.class);

    private final AsteriskAccountRepository asteriskAccountRepository;

    private final AsteriskAccountMapper asteriskAccountMapper;

    public AsteriskAccountService(AsteriskAccountRepository asteriskAccountRepository, AsteriskAccountMapper asteriskAccountMapper) {
        this.asteriskAccountRepository = asteriskAccountRepository;
        this.asteriskAccountMapper = asteriskAccountMapper;
    }

    /**
     * Save a asteriskAccount.
     *
     * @param asteriskAccountDTO the entity to save.
     * @return the persisted entity.
     */
    public AsteriskAccountDTO save(AsteriskAccountDTO asteriskAccountDTO) {
        log.debug("Request to save AsteriskAccount : {}", asteriskAccountDTO);
        AsteriskAccount asteriskAccount = asteriskAccountMapper.toEntity(asteriskAccountDTO);
        asteriskAccount = asteriskAccountRepository.save(asteriskAccount);
        return asteriskAccountMapper.toDto(asteriskAccount);
    }

    /**
     * Partially update a asteriskAccount.
     *
     * @param asteriskAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AsteriskAccountDTO> partialUpdate(AsteriskAccountDTO asteriskAccountDTO) {
        log.debug("Request to partially update AsteriskAccount : {}", asteriskAccountDTO);

        return asteriskAccountRepository
            .findById(asteriskAccountDTO.getId())
            .map(
                existingAsteriskAccount -> {
                    asteriskAccountMapper.partialUpdate(existingAsteriskAccount, asteriskAccountDTO);

                    return existingAsteriskAccount;
                }
            )
            .map(asteriskAccountRepository::save)
            .map(asteriskAccountMapper::toDto);
    }

    /**
     * Get all the asteriskAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AsteriskAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AsteriskAccounts");
        return asteriskAccountRepository.findAll(pageable).map(asteriskAccountMapper::toDto);
    }

    /**
     *  Get all the asteriskAccounts where VoipAccount is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AsteriskAccountDTO> findAllWhereVoipAccountIsNull() {
        log.debug("Request to get all asteriskAccounts where VoipAccount is null");
        return StreamSupport
            .stream(asteriskAccountRepository.findAll().spliterator(), false)
            .filter(asteriskAccount -> asteriskAccount.getVoipAccount() == null)
            .map(asteriskAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one asteriskAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AsteriskAccountDTO> findOne(Long id) {
        log.debug("Request to get AsteriskAccount : {}", id);
        return asteriskAccountRepository.findById(id).map(asteriskAccountMapper::toDto);
    }

    /**
     * Delete the asteriskAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AsteriskAccount : {}", id);
        asteriskAccountRepository.deleteById(id);
    }
}
