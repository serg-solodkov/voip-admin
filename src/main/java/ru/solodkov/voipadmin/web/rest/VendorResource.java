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
import ru.solodkov.voipadmin.repository.VendorRepository;
import ru.solodkov.voipadmin.service.VendorService;
import ru.solodkov.voipadmin.service.dto.VendorDTO;
import ru.solodkov.voipadmin.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ru.solodkov.voipadmin.domain.Vendor}.
 */
@RestController
@RequestMapping("/api")
public class VendorResource {

    private final Logger log = LoggerFactory.getLogger(VendorResource.class);

    private static final String ENTITY_NAME = "vendor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendorService vendorService;

    private final VendorRepository vendorRepository;

    public VendorResource(VendorService vendorService, VendorRepository vendorRepository) {
        this.vendorService = vendorService;
        this.vendorRepository = vendorRepository;
    }

    /**
     * {@code POST  /vendors} : Create a new vendor.
     *
     * @param vendorDTO the vendorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vendorDTO, or with status {@code 400 (Bad Request)} if the vendor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vendors")
    public ResponseEntity<VendorDTO> createVendor(@RequestBody VendorDTO vendorDTO) throws URISyntaxException {
        log.debug("REST request to save Vendor : {}", vendorDTO);
        if (vendorDTO.getId() != null) {
            throw new BadRequestAlertException("A new vendor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VendorDTO result = vendorService.save(vendorDTO);
        return ResponseEntity
            .created(new URI("/api/vendors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vendors/:id} : Updates an existing vendor.
     *
     * @param id the id of the vendorDTO to save.
     * @param vendorDTO the vendorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendorDTO,
     * or with status {@code 400 (Bad Request)} if the vendorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vendorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vendors/{id}")
    public ResponseEntity<VendorDTO> updateVendor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VendorDTO vendorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Vendor : {}, {}", id, vendorDTO);
        if (vendorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VendorDTO result = vendorService.save(vendorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vendors/:id} : Partial updates given fields of an existing vendor, field will ignore if it is null
     *
     * @param id the id of the vendorDTO to save.
     * @param vendorDTO the vendorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendorDTO,
     * or with status {@code 400 (Bad Request)} if the vendorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vendorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vendorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vendors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<VendorDTO> partialUpdateVendor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VendorDTO vendorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vendor partially : {}, {}", id, vendorDTO);
        if (vendorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VendorDTO> result = vendorService.partialUpdate(vendorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vendorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vendors} : get all the vendors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendors in body.
     */
    @GetMapping("/vendors")
    public ResponseEntity<List<VendorDTO>> getAllVendors(Pageable pageable) {
        log.debug("REST request to get a page of Vendors");
        Page<VendorDTO> page = vendorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vendors/:id} : get the "id" vendor.
     *
     * @param id the id of the vendorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vendorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vendors/{id}")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable Long id) {
        log.debug("REST request to get Vendor : {}", id);
        Optional<VendorDTO> vendorDTO = vendorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vendorDTO);
    }

    /**
     * {@code DELETE  /vendors/:id} : delete the "id" vendor.
     *
     * @param id the id of the vendorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vendors/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        log.debug("REST request to delete Vendor : {}", id);
        vendorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
