package com.wms.warehouse.repository;

import com.wms.warehouse.entity.DockDoor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DockDoorRepository extends JpaRepository<DockDoor, Long> {

    List<DockDoor> findByWarehouseId(Long warehouseId);

    List<DockDoor> findByWarehouseIdAndStatus(Long warehouseId, DockDoor.DockDoorStatus status);

    boolean existsByWarehouseIdAndCode(Long warehouseId, String code);
}
