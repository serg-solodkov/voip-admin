package ru.solodkov.voipadmin.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.ResponsiblePerson;

/**
 * Spring Data SQL repository for the ResponsiblePerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsiblePersonRepository extends JpaRepository<ResponsiblePerson, Long> {}
