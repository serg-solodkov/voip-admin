package ru.solodkov.voipadmin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.OtherDeviceType;

/**
 * Spring Data SQL repository for the OtherDeviceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtherDeviceTypeRepository extends JpaRepository<OtherDeviceType, Long> {}
