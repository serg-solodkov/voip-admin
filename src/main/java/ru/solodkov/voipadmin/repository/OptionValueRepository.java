package ru.solodkov.voipadmin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.OptionValue;

/**
 * Spring Data SQL repository for the OptionValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {}
