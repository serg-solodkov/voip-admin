package ru.solodkov.voipadmin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.AsteriskAccount;

/**
 * Spring Data SQL repository for the AsteriskAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AsteriskAccountRepository extends JpaRepository<AsteriskAccount, Long> {}
