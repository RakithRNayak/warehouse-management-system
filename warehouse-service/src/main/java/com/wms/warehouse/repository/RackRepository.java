package com.wms.warehouse.repository;

import com.wms.warehouse.entity.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {

    List<Rack> findByAisleId(Long aisleId);

    boolean existsByAisleIdAndCode(Long aisleId, String code);
}
