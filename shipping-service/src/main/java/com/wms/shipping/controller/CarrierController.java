package com.wms.shipping.controller;

import com.wms.shipping.entity.Carrier;
import com.wms.shipping.exception.ResourceNotFoundException;
import com.wms.shipping.repository.CarrierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carriers")
public class CarrierController {

    private final CarrierRepository carrierRepository;

    public CarrierController(CarrierRepository carrierRepository) {
        this.carrierRepository = carrierRepository;
    }

    @GetMapping
    public ResponseEntity<List<Carrier>> getAllCarriers() {
        return ResponseEntity.ok(carrierRepository.findByIsActiveTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrier> getCarrierById(@PathVariable Long id) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrier not found with id: " + id));
        return ResponseEntity.ok(carrier);
    }

    @PostMapping
    public ResponseEntity<Carrier> createCarrier(@RequestBody Carrier carrier) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(carrierRepository.save(carrier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carrier> updateCarrier(@PathVariable Long id, @RequestBody Carrier carrierDetails) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrier not found with id: " + id));
        carrier.setName(carrierDetails.getName());
        carrier.setContactName(carrierDetails.getContactName());
        carrier.setContactPhone(carrierDetails.getContactPhone());
        carrier.setContactEmail(carrierDetails.getContactEmail());
        carrier.setTrackingUrlTemplate(carrierDetails.getTrackingUrlTemplate());
        return ResponseEntity.ok(carrierRepository.save(carrier));
    }
}
