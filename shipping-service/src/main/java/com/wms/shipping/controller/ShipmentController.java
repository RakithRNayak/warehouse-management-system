package com.wms.shipping.controller;

import com.wms.shipping.entity.Shipment;
import com.wms.shipping.service.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<Page<Shipment>> getAllShipments(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(shipmentService.getAllShipments(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(@PathVariable Long id) {
        return ResponseEntity.ok(shipmentService.getShipmentById(id));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<Shipment> getShipmentByTracking(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(shipmentService.getShipmentByTrackingNumber(trackingNumber));
    }

    @PostMapping
    public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(shipmentService.createShipment(shipment));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Shipment> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        Shipment.ShipmentStatus status = Shipment.ShipmentStatus.valueOf(request.get("status"));
        String location = request.get("location");
        String description = request.get("description");
        return ResponseEntity.ok(shipmentService.updateStatus(id, status, location, description));
    }
}
