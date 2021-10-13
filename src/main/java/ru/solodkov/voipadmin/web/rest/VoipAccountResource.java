package ru.solodkov.voipadmin.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.solodkov.voipadmin.repository.VoipAccountRepository;
import ru.solodkov.voipadmin.service.VoipAccountService;
import ru.solodkov.voipadmin.service.dto.VoipAccountDTO;
import ru.solodkov.voipadmin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.solodkov.voipadmin.domain.VoipAccount}.
 */
@RestController
@RequestMapping("/api")
public class VoipAccountResource {

    private final Logger log = LoggerFactory.getLogger(VoipAccountResource.class);

    private static final String ENTITY_NAME = "voipAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoipAccountService voipAccountService;

    private final VoipAccountRepository voipAccountRepository;

    public VoipAccountResource(VoipAccountService voipAccountService, VoipAccountRepository voipAccountRepository) {
        this.voipAccountService = voipAccountService;
        this.voipAccountRepository = voipAccountRepository;
    }

    /**
     * {@code POST  /voip-accounts} : Create a new voipAccount.
     *
     * @param voipAccountDTO the voipAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voipAccountDTO, or with status {@code 400 (Bad Request)} if the voipAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voip-accounts")
    public ResponseEntity<VoipAccountDTO> createVoipAccount(@Valid @RequestBody VoipAccountDTO voipAccountDTO) throws URISyntaxException {
        log.debug("REST request to save VoipAccount : {}", voipAccountDTO);
        if (voipAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new voipAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VoipAccountDTO result = voipAccountService.save(voipAccountDTO);
        return ResponseEntity
            .created(new URI("/api/voip-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /voip-accounts/:id} : Updates an existing voipAccount.
     *
     * @param id the id of the voipAccountDTO to save.
     * @param voipAccountDTO the voipAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voipAccountDTO,
     * or with status {@code 400 (Bad Request)} if the voipAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voipAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voip-accounts/{id}")
    public ResponseEntity<VoipAccountDTO> updateVoipAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VoipAccountDTO voipAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VoipAccount : {}, {}", id, voipAccountDTO);
        if (voipAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voipAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voipAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VoipAccountDTO result = voipAccountService.save(voipAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voipAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /voip-accounts/:id} : Partial updates given fields of an existing voipAccount, field will ignore if it is null
     *
     * @param id the id of the voipAccountDTO to save.
     * @param voipAccountDTO the voipAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voipAccountDTO,
     * or with status {@code 400 (Bad Request)} if the voipAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the voipAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the voipAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/voip-accounts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VoipAccountDTO> partialUpdateVoipAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VoipAccountDTO voipAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VoipAccount partially : {}, {}", id, voipAccountDTO);
        if (voipAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voipAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voipAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoipAccountDTO> result = voipAccountService.partialUpdate(voipAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voipAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /voip-accounts} : get all the voipAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voipAccounts in body.
     */
    @GetMapping("/voip-accounts")
    public ResponseEntity<List<VoipAccountDTO>> getAllVoipAccounts(Pageable pageable) {
        log.debug("REST request to get a page of VoipAccounts");
        Page<VoipAccountDTO> page = voipAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /voip-accounts/:id} : get the "id" voipAccount.
     *
     * @param id the id of the voipAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voipAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voip-accounts/{id}")
    public ResponseEntity<VoipAccountDTO> getVoipAccount(@PathVariable Long id) {
        log.debug("REST request to get VoipAccount : {}", id);
        Optional<VoipAccountDTO> voipAccountDTO = voipAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(voipAccountDTO);
    }

    /**
     * {@code DELETE  /voip-accounts/:id} : delete the "id" voipAccount.
     *
     * @param id the id of the voipAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voip-accounts/{id}")
    public ResponseEntity<Void> deleteVoipAccount(@PathVariable Long id) {
        log.debug("REST request to delete VoipAccount : {}", id);
        voipAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
