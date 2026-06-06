package com.wms.shipping.service;

import com.wms.shipping.entity.Shipment;
import com.wms.shipping.entity.TrackingEvent;
import com.wms.shipping.exception.ResourceNotFoundException;
import com.wms.shipping.repository.ShipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ShipmentService(ShipmentRepository shipmentRepository,
                            KafkaTemplate<String, Object> kafkaTemplate) {
        this.shipmentRepository = shipmentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional(readOnly = true)
    public Page<Shipment> getAllShipments(Pageable pageable) {
        return shipmentRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Shipment getShipmentById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Shipment getShipmentByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with tracking: " + trackingNumber));
    }

    @Transactional
    public Shipment createShipment(Shipment shipment) {
        shipment.setShipmentNumber("SHP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Shipment saved = shipmentRepository.save(shipment);

        kafkaTemplate.send("wms.shipping.shipment-created", Map.of(
                "shipmentId", saved.getId(),
                "shipmentNumber", saved.getShipmentNumber(),
                "orderId", saved.getOutboundOrderId()
        ));

        return saved;
    }

    @Transactional
    public Shipment updateStatus(Long id, Shipment.ShipmentStatus status, String location, String description) {
        Shipment shipment = getShipmentById(id);
        shipment.setStatus(status);

        if (status == Shipment.ShipmentStatus.PICKED_UP || status == Shipment.ShipmentStatus.IN_TRANSIT) {
            shipment.setShippedAt(LocalDateTime.now());
        }

        TrackingEvent event = TrackingEvent.builder()
                .shipment(shipment)
                .status(status)
                .location(location)
                .description(description)
                .eventTime(LocalDateTime.now())
                .build();
        shipment.getTrackingEvents().add(event);

        Shipment updated = shipmentRepository.save(shipment);

        kafkaTemplate.send("wms.shipping.status-updated", Map.of(
                "shipmentId", updated.getId(),
                "shipmentNumber", updated.getShipmentNumber(),
                "status", status.name(),
                "orderId", updated.getOutboundOrderId()
        ));

        return updated;
    }
}
