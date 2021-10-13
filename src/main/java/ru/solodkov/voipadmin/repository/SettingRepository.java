package ru.solodkov.voipadmin.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.Setting;

/**
 * Spring Data SQL repository for the Setting entity.
 */
@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    @Query(
        value = "select distinct setting from Setting setting left join fetch setting.selectedValues",
        countQuery = "select count(distinct setting) from Setting setting"
    )
    Page<Setting> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct setting from Setting setting left join fetch setting.selectedValues")
    List<Setting> findAllWithEagerRelationships();

    @Query("select setting from Setting setting left join fetch setting.selectedValues where setting.id =:id")
    Optional<Setting> findOneWithEagerRelationships(@Param("id") Long id);
}
