package ru.solodkov.voipadmin.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.domain.VoipAccount;
import ru.solodkov.voipadmin.repository.VoipAccountRepository;
import ru.solodkov.voipadmin.service.dto.VoipAccountDTO;
import ru.solodkov.voipadmin.service.mapper.VoipAccountMapper;

/**
 * Service Implementation for managing {@link VoipAccount}.
 */
@Service
@Transactional
public class VoipAccountService {

    private final Logger log = LoggerFactory.getLogger(VoipAccountService.class);

    private final VoipAccountRepository voipAccountRepository;

    private final VoipAccountMapper voipAccountMapper;

    public VoipAccountService(VoipAccountRepository voipAccountRepository, VoipAccountMapper voipAccountMapper) {
        this.voipAccountRepository = voipAccountRepository;
        this.voipAccountMapper = voipAccountMapper;
    }

    /**
     * Save a voipAccount.
     *
     * @param voipAccountDTO the entity to save.
     * @return the persisted entity.
     */
    public VoipAccountDTO save(VoipAccountDTO voipAccountDTO) {
        log.debug("Request to save VoipAccount : {}", voipAccountDTO);
        VoipAccount voipAccount = voipAccountMapper.toEntity(voipAccountDTO);
        voipAccount = voipAccountRepository.save(voipAccount);
        return voipAccountMapper.toDto(voipAccount);
    }

    /**
     * Partially update a voipAccount.
     *
     * @param voipAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VoipAccountDTO> partialUpdate(VoipAccountDTO voipAccountDTO) {
        log.debug("Request to partially update VoipAccount : {}", voipAccountDTO);

        return voipAccountRepository
            .findById(voipAccountDTO.getId())
            .map(
                existingVoipAccount -> {
                    voipAccountMapper.partialUpdate(existingVoipAccount, voipAccountDTO);

                    return existingVoipAccount;
                }
            )
            .map(voipAccountRepository::save)
            .map(voipAccountMapper::toDto);
    }

    /**
     * Get all the voipAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VoipAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VoipAccounts");
        return voipAccountRepository.findAll(pageable).map(voipAccountMapper::toDto);
    }

    /**
     * Get one voipAccount by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VoipAccountDTO> findOne(Long id) {
        log.debug("Request to get VoipAccount : {}", id);
        return voipAccountRepository.findById(id).map(voipAccountMapper::toDto);
    }

    /**
     * Delete the voipAccount by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VoipAccount : {}", id);
        voipAccountRepository.deleteById(id);
    }
}
