package ru.solodkov.voipadmin.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.solodkov.voipadmin.domain.DeviceModel;

/**
 * Spring Data SQL repository for the DeviceModel entity.
 */
@Repository
public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {
    @Query(
        value = "select distinct deviceModel from DeviceModel deviceModel left join fetch deviceModel.options",
        countQuery = "select count(distinct deviceModel) from DeviceModel deviceModel"
    )
    Page<DeviceModel> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct deviceModel from DeviceModel deviceModel left join fetch deviceModel.options")
    List<DeviceModel> findAllWithEagerRelationships();

    @Query("select deviceModel from DeviceModel deviceModel left join fetch deviceModel.options where deviceModel.id =:id")
    Optional<DeviceModel> findOneWithEagerRelationships(@Param("id") Long id);
}
