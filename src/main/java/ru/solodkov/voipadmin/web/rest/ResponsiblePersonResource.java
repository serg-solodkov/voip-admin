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
import ru.solodkov.voipadmin.repository.ResponsiblePersonRepository;
import ru.solodkov.voipadmin.service.ResponsiblePersonService;
import ru.solodkov.voipadmin.service.dto.ResponsiblePersonDTO;
import ru.solodkov.voipadmin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.solodkov.voipadmin.domain.ResponsiblePerson}.
 */
@RestController
@RequestMapping("/api")
public class ResponsiblePersonResource {

    private final Logger log = LoggerFactory.getLogger(ResponsiblePersonResource.class);

    private static final String ENTITY_NAME = "responsiblePerson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResponsiblePersonService responsiblePersonService;

    private final ResponsiblePersonRepository responsiblePersonRepository;

    public ResponsiblePersonResource(
        ResponsiblePersonService responsiblePersonService,
        ResponsiblePersonRepository responsiblePersonRepository
    ) {
        this.responsiblePersonService = responsiblePersonService;
        this.responsiblePersonRepository = responsiblePersonRepository;
    }

    /**
     * {@code POST  /responsible-people} : Create a new responsiblePerson.
     *
     * @param responsiblePersonDTO the responsiblePersonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new responsiblePersonDTO, or with status {@code 400 (Bad Request)} if the responsiblePerson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/responsible-people")
    public ResponseEntity<ResponsiblePersonDTO> createResponsiblePerson(@Valid @RequestBody ResponsiblePersonDTO responsiblePersonDTO)
        throws URISyntaxException {
        log.debug("REST request to save ResponsiblePerson : {}", responsiblePersonDTO);
        if (responsiblePersonDTO.getId() != null) {
            throw new BadRequestAlertException("A new responsiblePerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResponsiblePersonDTO result = responsiblePersonService.save(responsiblePersonDTO);
        return ResponseEntity
            .created(new URI("/api/responsible-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /responsible-people/:id} : Updates an existing responsiblePerson.
     *
     * @param id the id of the responsiblePersonDTO to save.
     * @param responsiblePersonDTO the responsiblePersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsiblePersonDTO,
     * or with status {@code 400 (Bad Request)} if the responsiblePersonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the responsiblePersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/responsible-people/{id}")
    public ResponseEntity<ResponsiblePersonDTO> updateResponsiblePerson(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResponsiblePersonDTO responsiblePersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ResponsiblePerson : {}, {}", id, responsiblePersonDTO);
        if (responsiblePersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsiblePersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsiblePersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResponsiblePersonDTO result = responsiblePersonService.save(responsiblePersonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsiblePersonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /responsible-people/:id} : Partial updates given fields of an existing responsiblePerson, field will ignore if it is null
     *
     * @param id the id of the responsiblePersonDTO to save.
     * @param responsiblePersonDTO the responsiblePersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsiblePersonDTO,
     * or with status {@code 400 (Bad Request)} if the responsiblePersonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the responsiblePersonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the responsiblePersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/responsible-people/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ResponsiblePersonDTO> partialUpdateResponsiblePerson(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResponsiblePersonDTO responsiblePersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResponsiblePerson partially : {}, {}", id, responsiblePersonDTO);
        if (responsiblePersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsiblePersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsiblePersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResponsiblePersonDTO> result = responsiblePersonService.partialUpdate(responsiblePersonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsiblePersonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /responsible-people} : get all the responsiblePeople.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of responsiblePeople in body.
     */
    @GetMapping("/responsible-people")
    public ResponseEntity<List<ResponsiblePersonDTO>> getAllResponsiblePeople(Pageable pageable) {
        log.debug("REST request to get a page of ResponsiblePeople");
        Page<ResponsiblePersonDTO> page = responsiblePersonService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /responsible-people/:id} : get the "id" responsiblePerson.
     *
     * @param id the id of the responsiblePersonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the responsiblePersonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/responsible-people/{id}")
    public ResponseEntity<ResponsiblePersonDTO> getResponsiblePerson(@PathVariable Long id) {
        log.debug("REST request to get ResponsiblePerson : {}", id);
        Optional<ResponsiblePersonDTO> responsiblePersonDTO = responsiblePersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(responsiblePersonDTO);
    }

    /**
     * {@code DELETE  /responsible-people/:id} : delete the "id" responsiblePerson.
     *
     * @param id the id of the responsiblePersonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/responsible-people/{id}")
    public ResponseEntity<Void> deleteResponsiblePerson(@PathVariable Long id) {
        log.debug("REST request to delete ResponsiblePerson : {}", id);
        responsiblePersonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
