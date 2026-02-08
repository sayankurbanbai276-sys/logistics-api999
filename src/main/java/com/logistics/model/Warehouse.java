package com.logistics.model;

/**
 * Warehouse entity for storage management
 * Demonstrates composition relationship with Shipments
 */
public class Warehouse extends BaseEntity {
    private String location;
    private Integer capacity;
    private Integer currentLoad;

    public Warehouse() {
        super();
        this.currentLoad = 0;
    }

    public Warehouse(Integer id, String name, String location, Integer capacity) {
        super(id, name);
        this.location = location;
        this.capacity = capacity;
        this.currentLoad = 0;
    }

    @Override
    public String getEntityType() {
        return "WAREHOUSE";
    }

    @Override
    public boolean validate() {
        return location != null && !location.isEmpty()
                && capacity != null && capacity > 0
                && currentLoad >= 0 && currentLoad <= capacity;
    }

    public boolean hasAvailableSpace(Integer requiredSpace) {
        return (capacity - currentLoad) >= requiredSpace;
    }

    public Double getUtilizationPercentage() {
        return (currentLoad.doubleValue() / capacity.doubleValue()) * 100;
    }

    // Getters and Setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        if (capacity != null && capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
    }

    public Integer getCurrentLoad() {
        return currentLoad;
    }

    public void setCurrentLoad(Integer currentLoad) {
        if (currentLoad != null && currentLoad < 0) {
            throw new IllegalArgumentException("Current load cannot be negative");
        }
        this.currentLoad = currentLoad;
    }
}