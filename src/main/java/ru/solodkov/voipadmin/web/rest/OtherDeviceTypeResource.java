package ru.solodkov.voipadmin.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import ru.solodkov.voipadmin.repository.OtherDeviceTypeRepository;
import ru.solodkov.voipadmin.service.OtherDeviceTypeService;
import ru.solodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import ru.solodkov.voipadmin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.solodkov.voipadmin.domain.OtherDeviceType}.
 */
@RestController
@RequestMapping("/api")
public class OtherDeviceTypeResource {

    private final Logger log = LoggerFactory.getLogger(OtherDeviceTypeResource.class);

    private static final String ENTITY_NAME = "otherDeviceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtherDeviceTypeService otherDeviceTypeService;

    private final OtherDeviceTypeRepository otherDeviceTypeRepository;

    public OtherDeviceTypeResource(OtherDeviceTypeService otherDeviceTypeService, OtherDeviceTypeRepository otherDeviceTypeRepository) {
        this.otherDeviceTypeService = otherDeviceTypeService;
        this.otherDeviceTypeRepository = otherDeviceTypeRepository;
    }

    /**
     * {@code POST  /other-device-types} : Create a new otherDeviceType.
     *
     * @param otherDeviceTypeDTO the otherDeviceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otherDeviceTypeDTO, or with status {@code 400 (Bad Request)} if the otherDeviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/other-device-types")
    public ResponseEntity<OtherDeviceTypeDTO> createOtherDeviceType(@RequestBody OtherDeviceTypeDTO otherDeviceTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save OtherDeviceType : {}", otherDeviceTypeDTO);
        if (otherDeviceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new otherDeviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OtherDeviceTypeDTO result = otherDeviceTypeService.save(otherDeviceTypeDTO);
        return ResponseEntity
            .created(new URI("/api/other-device-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /other-device-types/:id} : Updates an existing otherDeviceType.
     *
     * @param id the id of the otherDeviceTypeDTO to save.
     * @param otherDeviceTypeDTO the otherDeviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otherDeviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the otherDeviceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otherDeviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/other-device-types/{id}")
    public ResponseEntity<OtherDeviceTypeDTO> updateOtherDeviceType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OtherDeviceTypeDTO otherDeviceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OtherDeviceType : {}, {}", id, otherDeviceTypeDTO);
        if (otherDeviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otherDeviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otherDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OtherDeviceTypeDTO result = otherDeviceTypeService.save(otherDeviceTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otherDeviceTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /other-device-types/:id} : Partial updates given fields of an existing otherDeviceType, field will ignore if it is null
     *
     * @param id the id of the otherDeviceTypeDTO to save.
     * @param otherDeviceTypeDTO the otherDeviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otherDeviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the otherDeviceTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the otherDeviceTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the otherDeviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/other-device-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OtherDeviceTypeDTO> partialUpdateOtherDeviceType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OtherDeviceTypeDTO otherDeviceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OtherDeviceType partially : {}, {}", id, otherDeviceTypeDTO);
        if (otherDeviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otherDeviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otherDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OtherDeviceTypeDTO> result = otherDeviceTypeService.partialUpdate(otherDeviceTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otherDeviceTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /other-device-types} : get all the otherDeviceTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of otherDeviceTypes in body.
     */
    @GetMapping("/other-device-types")
    public ResponseEntity<List<OtherDeviceTypeDTO>> getAllOtherDeviceTypes(Pageable pageable) {
        log.debug("REST request to get a page of OtherDeviceTypes");
        Page<OtherDeviceTypeDTO> page = otherDeviceTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /other-device-types/:id} : get the "id" otherDeviceType.
     *
     * @param id the id of the otherDeviceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otherDeviceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/other-device-types/{id}")
    public ResponseEntity<OtherDeviceTypeDTO> getOtherDeviceType(@PathVariable Long id) {
        log.debug("REST request to get OtherDeviceType : {}", id);
        Optional<OtherDeviceTypeDTO> otherDeviceTypeDTO = otherDeviceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otherDeviceTypeDTO);
    }

    /**
     * {@code DELETE  /other-device-types/:id} : delete the "id" otherDeviceType.
     *
     * @param id the id of the otherDeviceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/other-device-types/{id}")
    public ResponseEntity<Void> deleteOtherDeviceType(@PathVariable Long id) {
        log.debug("REST request to delete OtherDeviceType : {}", id);
        otherDeviceTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
