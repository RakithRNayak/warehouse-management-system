package com.wms.order.repository;

import com.wms.order.entity.PickList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickListRepository extends JpaRepository<PickList, Long> {

    Optional<PickList> findByPickListNumber(String pickListNumber);

    Page<PickList> findByWarehouseIdAndStatusOrderByCreatedAtDesc(
            Long warehouseId, PickList.PickListStatus status, Pageable pageable);

    Page<PickList> findByAssignedToAndStatusOrderByCreatedAtDesc(
            Long assignedTo, PickList.PickListStatus status, Pageable pageable);

    Page<PickList> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
