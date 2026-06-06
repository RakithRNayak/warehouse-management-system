package com.wms.shipping.repository;

import com.wms.shipping.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findByShipmentNumber(String shipmentNumber);

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    Page<Shipment> findByOutboundOrderId(Long outboundOrderId, Pageable pageable);

    Page<Shipment> findByWarehouseIdOrderByCreatedAtDesc(Long warehouseId, Pageable pageable);

    Page<Shipment> findByStatusOrderByCreatedAtDesc(Shipment.ShipmentStatus status, Pageable pageable);

    Page<Shipment> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
