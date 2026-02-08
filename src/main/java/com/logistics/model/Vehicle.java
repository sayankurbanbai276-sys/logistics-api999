package com.logistics.model;

/**
 * Abstract Vehicle class representing different transport types
 */
public abstract class Vehicle extends BaseEntity {
    private String licensePlate;
    private Double capacity;
    private String status;

    public Vehicle() {
        super();
        this.status = "AVAILABLE";
    }

    public Vehicle(Integer id, String name, String licensePlate, Double capacity) {
        super(id, name);
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.status = "AVAILABLE";
    }

    @Override
    public boolean validate() {
        return licensePlate != null && !licensePlate.isEmpty()
                && capacity != null && capacity > 0;
    }

    // Abstract methods
    public abstract String getVehicleType();
    public abstract Double getOperatingCost();

    // Getters and Setters
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        if (capacity != null && capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}