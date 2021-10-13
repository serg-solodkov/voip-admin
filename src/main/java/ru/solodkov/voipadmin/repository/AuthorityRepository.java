package ru.solodkov.voipadmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.solodkov.voipadmin.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
