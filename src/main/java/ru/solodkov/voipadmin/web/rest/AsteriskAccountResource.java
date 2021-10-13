package ru.solodkov.voipadmin.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
import ru.solodkov.voipadmin.repository.AsteriskAccountRepository;
import ru.solodkov.voipadmin.service.AsteriskAccountService;
import ru.solodkov.voipadmin.service.dto.AsteriskAccountDTO;
import ru.solodkov.voipadmin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.solodkov.voipadmin.domain.AsteriskAccount}.
 */
@RestController
@RequestMapping("/api")
public class AsteriskAccountResource {

    private final Logger log = LoggerFactory.getLogger(AsteriskAccountResource.class);

    private static final String ENTITY_NAME = "asteriskAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AsteriskAccountService asteriskAccountService;

    private final AsteriskAccountRepository asteriskAccountRepository;

    public AsteriskAccountResource(AsteriskAccountService asteriskAccountService, AsteriskAccountRepository asteriskAccountRepository) {
        this.asteriskAccountService = asteriskAccountService;
        this.asteriskAccountRepository = asteriskAccountRepository;
    }

    /**
     * {@code POST  /asterisk-accounts} : Create a new asteriskAccount.
     *
     * @param asteriskAccountDTO the asteriskAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asteriskAccountDTO, or with status {@code 400 (Bad Request)} if the asteriskAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asterisk-accounts")
    public ResponseEntity<AsteriskAccountDTO> createAsteriskAccount(@RequestBody AsteriskAccountDTO asteriskAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save AsteriskAccount : {}", asteriskAccountDTO);
        if (asteriskAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new asteriskAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AsteriskAccountDTO result = asteriskAccountService.save(asteriskAccountDTO);
        return ResponseEntity
            .created(new URI("/api/asterisk-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asterisk-accounts/:id} : Updates an existing asteriskAccount.
     *
     * @param id the id of the asteriskAccountDTO to save.
     * @param asteriskAccountDTO the asteriskAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asteriskAccountDTO,
     * or with status {@code 400 (Bad Request)} if the asteriskAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asteriskAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asterisk-accounts/{id}")
    public ResponseEntity<AsteriskAccountDTO> updateAsteriskAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AsteriskAccountDTO asteriskAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AsteriskAccount : {}, {}", id, asteriskAccountDTO);
        if (asteriskAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asteriskAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asteriskAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AsteriskAccountDTO result = asteriskAccountService.save(asteriskAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asteriskAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asterisk-accounts/:id} : Partial updates given fields of an existing asteriskAccount, field will ignore if it is null
     *
     * @param id the id of the asteriskAccountDTO to save.
     * @param asteriskAccountDTO the asteriskAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asteriskAccountDTO,
     * or with status {@code 400 (Bad Request)} if the asteriskAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the asteriskAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the asteriskAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asterisk-accounts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AsteriskAccountDTO> partialUpdateAsteriskAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AsteriskAccountDTO asteriskAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AsteriskAccount partially : {}, {}", id, asteriskAccountDTO);
        if (asteriskAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asteriskAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asteriskAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AsteriskAccountDTO> result = asteriskAccountService.partialUpdate(asteriskAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asteriskAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asterisk-accounts} : get all the asteriskAccounts.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of asteriskAccounts in body.
     */
    @GetMapping("/asterisk-accounts")
    public ResponseEntity<List<AsteriskAccountDTO>> getAllAsteriskAccounts(
        Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("voipaccount-is-null".equals(filter)) {
            log.debug("REST request to get all AsteriskAccounts where voipAccount is null");
            return new ResponseEntity<>(asteriskAccountService.findAllWhereVoipAccountIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of AsteriskAccounts");
        Page<AsteriskAccountDTO> page = asteriskAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asterisk-accounts/:id} : get the "id" asteriskAccount.
     *
     * @param id the id of the asteriskAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asteriskAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asterisk-accounts/{id}")
    public ResponseEntity<AsteriskAccountDTO> getAsteriskAccount(@PathVariable Long id) {
        log.debug("REST request to get AsteriskAccount : {}", id);
        Optional<AsteriskAccountDTO> asteriskAccountDTO = asteriskAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asteriskAccountDTO);
    }

    /**
     * {@code DELETE  /asterisk-accounts/:id} : delete the "id" asteriskAccount.
     *
     * @param id the id of the asteriskAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asterisk-accounts/{id}")
    public ResponseEntity<Void> deleteAsteriskAccount(@PathVariable Long id) {
        log.debug("REST request to delete AsteriskAccount : {}", id);
        asteriskAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
