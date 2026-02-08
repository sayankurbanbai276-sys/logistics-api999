package com.logistics.model;

import java.time.LocalDate;

/**
 * Abstract Shipment class representing different types of shipments
 * Demonstrates inheritance and polymorphism
 */
public abstract class Shipment extends BaseEntity {
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

    public Shipment() {
        super();
        this.status = "PENDING";
        this.priority = "NORMAL";
    }

    public Shipment(Integer id, String name, String trackingNumber, String senderName,
                    String recipientName, String origin, String destination, Double weight) {
        super(id, name);
        this.trackingNumber = trackingNumber;
        this.senderName = senderName;
        this.recipientName = recipientName;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.status = "PENDING";
        this.priority = "NORMAL";
    }

    @Override
    public boolean validate() {
        return trackingNumber != null && !trackingNumber.isEmpty()
                && senderName != null && !senderName.isEmpty()
                && recipientName != null && !recipientName.isEmpty()
                && weight != null && weight > 0;
    }

    // Abstract methods for calculating shipping cost
    public abstract Double calculateShippingCost();

    // Abstract method for getting delivery time estimate
    public abstract Integer getEstimatedDeliveryDays();

    // Getters and Setters
    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if (weight != null && weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(LocalDate estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}