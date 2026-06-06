package com.wms.warehouse.repository;

import com.wms.warehouse.entity.Bin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BinRepository extends JpaRepository<Bin, Long> {

    Optional<Bin> findByCode(String code);

    List<Bin> findByRackId(Long rackId);

    boolean existsByCode(String code);

    @Query("SELECT b FROM Bin b WHERE b.rack.aisle.zone.warehouse.id = :warehouseId AND b.status = 'AVAILABLE'")
    List<Bin> findAvailableBinsByWarehouseId(@Param("warehouseId") Long warehouseId);

    List<Bin> findByStatus(Bin.BinStatus status);
}
