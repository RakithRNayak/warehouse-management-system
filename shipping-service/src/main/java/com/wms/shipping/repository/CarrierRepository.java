package com.wms.shipping.repository;

import com.wms.shipping.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long> {

    Optional<Carrier> findByCode(String code);

    boolean existsByCode(String code);

    List<Carrier> findByIsActiveTrue();
}
