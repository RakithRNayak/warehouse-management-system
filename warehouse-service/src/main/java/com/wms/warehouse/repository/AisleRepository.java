package com.wms.warehouse.repository;

import com.wms.warehouse.entity.Aisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AisleRepository extends JpaRepository<Aisle, Long> {

    List<Aisle> findByZoneId(Long zoneId);

    boolean existsByZoneIdAndCode(Long zoneId, String code);
}
