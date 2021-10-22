package ru.solodkov.voipadmin.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.Option;

/**
 * Spring Data SQL repository for the Option entity.
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long>, JpaSpecificationExecutor<Option> {
    @Query(
        value = "select distinct option from Option option left join fetch option.vendors",
        countQuery = "select count(distinct option) from Option option"
    )
    Page<Option> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct option from Option option left join fetch option.vendors")
    List<Option> findAllWithEagerRelationships();

    @Query("select option from Option option left join fetch option.vendors where option.id =:id")
    Optional<Option> findOneWithEagerRelationships(@Param("id") Long id);
}
