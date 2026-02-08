package com.logistics.model;

/**
 * Land Vehicle - Trucks, vans
 */
public class LandVehicle extends Vehicle {
    private String fuelType;

    public LandVehicle() {
        super();
    }

    public LandVehicle(Integer id, String name, String licensePlate,
                       Double capacity, String fuelType) {
        super(id, name, licensePlate, capacity);
        this.fuelType = fuelType;
    }

    @Override
    public String getEntityType() {
        return "LAND_VEHICLE";
    }

    @Override
    public String getVehicleType() {
        return "LAND";
    }

    @Override
    public Double getOperatingCost() {
        return getCapacity() * 0.8; // Lower cost for land transport
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}