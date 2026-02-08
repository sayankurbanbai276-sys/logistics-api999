package com.logistics.controller;

import com.logistics.dto.ShipmentDTO;
import com.logistics.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Shipment operations
 * Demonstrates RESTful API design principles
 */
@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    /**
     * GET /api/shipments - Get all shipments
     */
    @GetMapping
    public ResponseEntity<List<ShipmentDTO>> getAllShipments() {
        List<ShipmentDTO> shipments = shipmentService.getAllShipments();
        return ResponseEntity.ok(shipments);
    }

    /**
     * GET /api/shipments/{id} - Get shipment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDTO> getShipmentById(@PathVariable Integer id) {
        ShipmentDTO shipment = shipmentService.getShipmentById(id);
        return ResponseEntity.ok(shipment);
    }

    /**
     * GET /api/shipments/tracking/{trackingNumber} - Get shipment by tracking number
     */
    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<ShipmentDTO> getShipmentByTrackingNumber(@PathVariable String trackingNumber) {
        ShipmentDTO shipment = shipmentService.getShipmentByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(shipment);
    }

    /**
     * GET /api/shipments/status/{status} - Get shipments by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ShipmentDTO>> getShipmentsByStatus(@PathVariable String status) {
        List<ShipmentDTO> shipments = shipmentService.getShipmentsByStatus(status);
        return ResponseEntity.ok(shipments);
    }

    /**
     * POST /api/shipments - Create new shipment
     */
    @PostMapping
    public ResponseEntity<ShipmentDTO> createShipment(@RequestBody ShipmentDTO shipmentDTO) {
        ShipmentDTO created = shipmentService.createShipment(shipmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/shipments/{id} - Update shipment
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDTO> updateShipment(
            @PathVariable Integer id,
            @RequestBody ShipmentDTO shipmentDTO) {
        ShipmentDTO updated = shipmentService.updateShipment(id, shipmentDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/shipments/{id} - Delete shipment
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(@PathVariable Integer id) {
        shipmentService.deleteShipment(id);
        return ResponseEntity.noContent().build();
    }
}
