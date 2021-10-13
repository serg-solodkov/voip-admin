package ru.solodkov.voipadmin.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.solodkov.voipadmin.domain.Vendor;
import ru.solodkov.voipadmin.repository.VendorRepository;
import ru.solodkov.voipadmin.service.dto.VendorDTO;
import ru.solodkov.voipadmin.service.mapper.VendorMapper;

/**
 * Service Implementation for managing {@link Vendor}.
 */
@Service
@Transactional
public class VendorService {

    private final Logger log = LoggerFactory.getLogger(VendorService.class);

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    public VendorService(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    /**
     * Save a vendor.
     *
     * @param vendorDTO the entity to save.
     * @return the persisted entity.
     */
    public VendorDTO save(VendorDTO vendorDTO) {
        log.debug("Request to save Vendor : {}", vendorDTO);
        Vendor vendor = vendorMapper.toEntity(vendorDTO);
        vendor = vendorRepository.save(vendor);
        return vendorMapper.toDto(vendor);
    }

    /**
     * Partially update a vendor.
     *
     * @param vendorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VendorDTO> partialUpdate(VendorDTO vendorDTO) {
        log.debug("Request to partially update Vendor : {}", vendorDTO);

        return vendorRepository
            .findById(vendorDTO.getId())
            .map(
                existingVendor -> {
                    vendorMapper.partialUpdate(existingVendor, vendorDTO);

                    return existingVendor;
                }
            )
            .map(vendorRepository::save)
            .map(vendorMapper::toDto);
    }

    /**
     * Get all the vendors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VendorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Vendors");
        return vendorRepository.findAll(pageable).map(vendorMapper::toDto);
    }

    /**
     * Get one vendor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VendorDTO> findOne(Long id) {
        log.debug("Request to get Vendor : {}", id);
        return vendorRepository.findById(id).map(vendorMapper::toDto);
    }

    /**
     * Delete the vendor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vendor : {}", id);
        vendorRepository.deleteById(id);
    }
}
