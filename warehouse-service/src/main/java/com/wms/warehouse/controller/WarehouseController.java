package com.wms.warehouse.controller;

import com.wms.warehouse.entity.*;
import com.wms.warehouse.service.WarehouseManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseManagementService warehouseService;

    public WarehouseController(WarehouseManagementService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(warehouseService.createWarehouse(warehouse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(
            @PathVariable Long id, @RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, warehouse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Zone endpoints ----

    @GetMapping("/{warehouseId}/zones")
    public ResponseEntity<List<Zone>> getZones(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getZonesByWarehouseId(warehouseId));
    }

    @PostMapping("/{warehouseId}/zones")
    public ResponseEntity<Zone> createZone(
            @PathVariable Long warehouseId, @RequestBody Zone zone) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(warehouseService.createZone(warehouseId, zone));
    }

    // ---- Aisle endpoints ----

    @GetMapping("/zones/{zoneId}/aisles")
    public ResponseEntity<List<Aisle>> getAisles(@PathVariable Long zoneId) {
        return ResponseEntity.ok(warehouseService.getAislesByZoneId(zoneId));
    }

    @PostMapping("/zones/{zoneId}/aisles")
    public ResponseEntity<Aisle> createAisle(
            @PathVariable Long zoneId, @RequestBody Aisle aisle) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(warehouseService.createAisle(zoneId, aisle));
    }

    // ---- Rack endpoints ----

    @GetMapping("/aisles/{aisleId}/racks")
    public ResponseEntity<List<Rack>> getRacks(@PathVariable Long aisleId) {
        return ResponseEntity.ok(warehouseService.getRacksByAisleId(aisleId));
    }

    @PostMapping("/aisles/{aisleId}/racks")
    public ResponseEntity<Rack> createRack(
            @PathVariable Long aisleId, @RequestBody Rack rack) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(warehouseService.createRack(aisleId, rack));
    }

    // ---- Bin endpoints ----

    @GetMapping("/racks/{rackId}/bins")
    public ResponseEntity<List<Bin>> getBins(@PathVariable Long rackId) {
        return ResponseEntity.ok(warehouseService.getBinsByRackId(rackId));
    }

    @PostMapping("/racks/{rackId}/bins")
    public ResponseEntity<Bin> createBin(
            @PathVariable Long rackId, @RequestBody Bin bin) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(warehouseService.createBin(rackId, bin));
    }

    @GetMapping("/{warehouseId}/bins/available")
    public ResponseEntity<List<Bin>> getAvailableBins(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getAvailableBins(warehouseId));
    }

    // ---- Dock Door endpoints ----

    @GetMapping("/{warehouseId}/dock-doors")
    public ResponseEntity<List<DockDoor>> getDockDoors(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(warehouseService.getDockDoorsByWarehouseId(warehouseId));
    }

    @PostMapping("/{warehouseId}/dock-doors")
    public ResponseEntity<DockDoor> createDockDoor(
            @PathVariable Long warehouseId, @RequestBody DockDoor dockDoor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(warehouseService.createDockDoor(warehouseId, dockDoor));
    }
}
