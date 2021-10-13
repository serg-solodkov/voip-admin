package ru.solodkov.voipadmin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.VoipAccount;

/**
 * Spring Data SQL repository for the VoipAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoipAccountRepository extends JpaRepository<VoipAccount, Long> {}
