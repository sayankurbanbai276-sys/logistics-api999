package com.logistics.model;

/**
 * Sea Vehicle - Ships, boats
 */
public class SeaVehicle extends Vehicle {
    private String cargoType;

    public SeaVehicle() {
        super();
    }

    public SeaVehicle(Integer id, String name, String licensePlate,
                      Double capacity, String cargoType) {
        super(id, name, licensePlate, capacity);
        this.cargoType = cargoType;
    }

    @Override
    public String getEntityType() {
        return "SEA_VEHICLE";
    }

    @Override
    public String getVehicleType() {
        return "SEA";
    }

    @Override
    public Double getOperatingCost() {
        return getCapacity() * 1.2; // Medium cost for sea transport
    }

    public String getCargoType() {
        return cargoType;
    }

    public void setCargoType(String cargoType) {
        this.cargoType = cargoType;
    }
}