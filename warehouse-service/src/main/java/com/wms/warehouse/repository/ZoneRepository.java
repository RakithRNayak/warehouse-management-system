package com.wms.warehouse.repository;

import com.wms.warehouse.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    List<Zone> findByWarehouseId(Long warehouseId);

    boolean existsByWarehouseIdAndCode(Long warehouseId, String code);
}
