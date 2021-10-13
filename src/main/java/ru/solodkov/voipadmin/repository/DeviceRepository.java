package ru.solodkov.voipadmin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.Device;

/**
 * Spring Data SQL repository for the Device entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {}
