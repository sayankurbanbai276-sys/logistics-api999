package com.logistics.patterns.builder;

import com.logistics.model.*;
import com.logistics.patterns.factory.ShipmentFactory;

import java.time.LocalDate;

/**
 * BUILDER PATTERN - Shipment Builder
 *
 * Purpose: Simplifies construction of complex Shipment objects
 * Benefits:
 * - Fluent API for object creation
 * - Optional parameters handling
 * - Improved readability
 * - Validation before object creation
 */
public class ShipmentBuilder {

    private String shipmentType;
    private String trackingNumber;
    private String senderName;
    private String recipientName;
    private String origin;
    private String destination;
    private Double weight;
    private String status;
    private String priority;
    private LocalDate estimatedDelivery;
    private Integer vehicleId;
    private Integer warehouseId;
    private Boolean specialAttribute; // isFragile, temperatureControlled, or customsCleared

    private ShipmentFactory factory;

    public ShipmentBuilder() {
        this.factory = new ShipmentFactory();
        // Set defaults
        this.status = "PENDING";
        this.priority = "NORMAL";
    }

    public ShipmentBuilder type(String shipmentType) {
        this.shipmentType = shipmentType;
        return this;
    }

    public ShipmentBuilder trackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
        return this;
    }

    public ShipmentBuilder sender(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public ShipmentBuilder recipient(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public ShipmentBuilder from(String origin) {
        this.origin = origin;
        return this;
    }

    public ShipmentBuilder to(String destination) {
        this.destination = destination;
        return this;
    }

    public ShipmentBuilder weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public ShipmentBuilder status(String status) {
        this.status = status;
        return this;
    }

    public ShipmentBuilder priority(String priority) {
        this.priority = priority;
        return this;
    }

    public ShipmentBuilder estimatedDelivery(LocalDate estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
        return this;
    }

    public ShipmentBuilder vehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public ShipmentBuilder warehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
        return this;
    }

    public ShipmentBuilder specialAttribute(Boolean value) {
        this.specialAttribute = value;
        return this;
    }

    // Convenience methods for specific shipment types
    public ShipmentBuilder expressShipment() {
        this.shipmentType = "EXPRESS";
        this.priority = "HIGH";
        return this;
    }

    public ShipmentBuilder standardShipment() {
        this.shipmentType = "STANDARD";
        this.priority = "NORMAL";
        return this;
    }

    public ShipmentBuilder economyShipment() {
        this.shipmentType = "ECONOMY";
        this.priority = "LOW";
        return this;
    }

    public ShipmentBuilder fragile(boolean isFragile) {
        this.specialAttribute = isFragile;
        return this;
    }

    public ShipmentBuilder temperatureControlled(boolean value) {
        this.specialAttribute = value;
        return this;
    }

    public ShipmentBuilder customsCleared(boolean value) {
        this.specialAttribute = value;
        return this;
    }

    /**
     * Validates required fields before building
     */
    private void validate() {
        if (shipmentType == null || shipmentType.isEmpty()) {
            throw new IllegalStateException("Shipment type is required");
        }
        if (trackingNumber == null || trackingNumber.isEmpty()) {
            throw new IllegalStateException("Tracking number is required");
        }
        if (senderName == null || senderName.isEmpty()) {
            throw new IllegalStateException("Sender name is required");
        }
        if (recipientName == null || recipientName.isEmpty()) {
            throw new IllegalStateException("Recipient name is required");
        }
        if (weight == null || weight <= 0) {
            throw new IllegalStateException("Weight must be positive");
        }
    }

    /**
     * Builds the Shipment object
     */
    public Shipment build() {
        validate();

        // Use factory to create the appropriate shipment type
        Shipment shipment;
        if (specialAttribute != null) {
            shipment = factory.createShipmentWithAttributes(
                    shipmentType, trackingNumber, senderName, recipientName,
                    origin, destination, weight, specialAttribute
            );
        } else {
            shipment = factory.createShipment(
                    shipmentType, trackingNumber, senderName, recipientName,
                    origin, destination, weight
            );
        }

        // Set optional fields
        if (status != null) {
            shipment.setStatus(status);
        }
        if (priority != null) {
            shipment.setPriority(priority);
        }
        if (estimatedDelivery != null) {
            shipment.setEstimatedDelivery(estimatedDelivery);
        }
        if (vehicleId != null) {
            shipment.setVehicleId(vehicleId);
        }
        if (warehouseId != null) {
            shipment.setWarehouseId(warehouseId);
        }

        return shipment;
    }
}
