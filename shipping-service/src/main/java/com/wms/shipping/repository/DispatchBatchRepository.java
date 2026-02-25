package com.wms.shipping.repository;

import com.wms.shipping.entity.DispatchBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DispatchBatchRepository extends JpaRepository<DispatchBatch, Long> {

    Optional<DispatchBatch> findByBatchNumber(String batchNumber);

    Page<DispatchBatch> findByWarehouseIdAndStatusOrderByCreatedAtDesc(
            Long warehouseId, DispatchBatch.DispatchStatus status, Pageable pageable);

    Page<DispatchBatch> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
