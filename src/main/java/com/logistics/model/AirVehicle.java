package com.logistics.model;

/**
 * Air Vehicle - Planes, helicopters
 */
public class AirVehicle extends Vehicle {
    private Integer maxAltitude;

    public AirVehicle() {
        super();
    }

    public AirVehicle(Integer id, String name, String licensePlate,
                      Double capacity, Integer maxAltitude) {
        super(id, name, licensePlate, capacity);
        this.maxAltitude = maxAltitude;
    }

    @Override
    public String getEntityType() {
        return "AIR_VEHICLE";
    }

    @Override
    public String getVehicleType() {
        return "AIR";
    }

    @Override
    public Double getOperatingCost() {
        return getCapacity() * 2.5; // High cost for air transport
    }

    public Integer getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(Integer maxAltitude) {
        this.maxAltitude = maxAltitude;
    }
}