package com.wms.warehouse.service;

import com.wms.warehouse.entity.*;
import com.wms.warehouse.exception.DuplicateResourceException;
import com.wms.warehouse.exception.ResourceNotFoundException;
import com.wms.warehouse.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseManagementService {

    private final WarehouseRepository warehouseRepository;
    private final ZoneRepository zoneRepository;
    private final AisleRepository aisleRepository;
    private final RackRepository rackRepository;
    private final BinRepository binRepository;
    private final DockDoorRepository dockDoorRepository;

    public WarehouseManagementService(WarehouseRepository warehouseRepository,
                                       ZoneRepository zoneRepository,
                                       AisleRepository aisleRepository,
                                       RackRepository rackRepository,
                                       BinRepository binRepository,
                                       DockDoorRepository dockDoorRepository) {
        this.warehouseRepository = warehouseRepository;
        this.zoneRepository = zoneRepository;
        this.aisleRepository = aisleRepository;
        this.rackRepository = rackRepository;
        this.binRepository = binRepository;
        this.dockDoorRepository = dockDoorRepository;
    }

    // ---- Warehouse CRUD ----

    @Transactional(readOnly = true)
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
    }

    @Transactional
    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouseRepository.existsByCode(warehouse.getCode())) {
            throw new DuplicateResourceException("Warehouse with code " + warehouse.getCode() + " already exists");
        }
        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public Warehouse updateWarehouse(Long id, Warehouse warehouseDetails) {
        Warehouse warehouse = getWarehouseById(id);
        warehouse.setName(warehouseDetails.getName());
        warehouse.setType(warehouseDetails.getType());
        warehouse.setStreetAddress(warehouseDetails.getStreetAddress());
        warehouse.setCity(warehouseDetails.getCity());
        warehouse.setState(warehouseDetails.getState());
        warehouse.setCountry(warehouseDetails.getCountry());
        warehouse.setZipCode(warehouseDetails.getZipCode());
        warehouse.setContactName(warehouseDetails.getContactName());
        warehouse.setContactPhone(warehouseDetails.getContactPhone());
        warehouse.setContactEmail(warehouseDetails.getContactEmail());
        warehouse.setTotalAreaSqft(warehouseDetails.getTotalAreaSqft());
        return warehouseRepository.save(warehouse);
    }

    @Transactional
    public void deleteWarehouse(Long id) {
        Warehouse warehouse = getWarehouseById(id);
        warehouse.setIsActive(false);
        warehouseRepository.save(warehouse);
    }

    // ---- Zone CRUD ----

    @Transactional(readOnly = true)
    public List<Zone> getZonesByWarehouseId(Long warehouseId) {
        return zoneRepository.findByWarehouseId(warehouseId);
    }

    @Transactional
    public Zone createZone(Long warehouseId, Zone zone) {
        Warehouse warehouse = getWarehouseById(warehouseId);
        if (zoneRepository.existsByWarehouseIdAndCode(warehouseId, zone.getCode())) {
            throw new DuplicateResourceException("Zone with code " + zone.getCode() + " already exists in this warehouse");
        }
        zone.setWarehouse(warehouse);
        return zoneRepository.save(zone);
    }

    // ---- Aisle CRUD ----

    @Transactional(readOnly = true)
    public List<Aisle> getAislesByZoneId(Long zoneId) {
        return aisleRepository.findByZoneId(zoneId);
    }

    @Transactional
    public Aisle createAisle(Long zoneId, Aisle aisle) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + zoneId));
        aisle.setZone(zone);
        return aisleRepository.save(aisle);
    }

    // ---- Rack CRUD ----

    @Transactional(readOnly = true)
    public List<Rack> getRacksByAisleId(Long aisleId) {
        return rackRepository.findByAisleId(aisleId);
    }

    @Transactional
    public Rack createRack(Long aisleId, Rack rack) {
        Aisle aisle = aisleRepository.findById(aisleId)
                .orElseThrow(() -> new ResourceNotFoundException("Aisle not found with id: " + aisleId));
        rack.setAisle(aisle);
        return rackRepository.save(rack);
    }

    // ---- Bin CRUD ----

    @Transactional(readOnly = true)
    public List<Bin> getBinsByRackId(Long rackId) {
        return binRepository.findByRackId(rackId);
    }

    @Transactional(readOnly = true)
    public List<Bin> getAvailableBins(Long warehouseId) {
        return binRepository.findAvailableBinsByWarehouseId(warehouseId);
    }

    @Transactional
    public Bin createBin(Long rackId, Bin bin) {
        Rack rack = rackRepository.findById(rackId)
                .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + rackId));
        if (binRepository.existsByCode(bin.getCode())) {
            throw new DuplicateResourceException("Bin with code " + bin.getCode() + " already exists");
        }
        bin.setRack(rack);
        return binRepository.save(bin);
    }

    // ---- Dock Door ----

    @Transactional(readOnly = true)
    public List<DockDoor> getDockDoorsByWarehouseId(Long warehouseId) {
        return dockDoorRepository.findByWarehouseId(warehouseId);
    }

    @Transactional
    public DockDoor createDockDoor(Long warehouseId, DockDoor dockDoor) {
        Warehouse warehouse = getWarehouseById(warehouseId);
        dockDoor.setWarehouse(warehouse);
        return dockDoorRepository.save(dockDoor);
    }
}
