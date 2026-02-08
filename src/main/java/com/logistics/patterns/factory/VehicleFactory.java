package com.logistics.patterns.factory;

import com.logistics.model.*;
import org.springframework.stereotype.Component;

/**
 * FACTORY PATTERN - Vehicle Factory
 *
 * Purpose: Creates different types of Vehicle objects based on type
 */
@Component
public class VehicleFactory {

    /**
     * Creates a Vehicle object based on the specified type
     *
     * @param type The vehicle type (AIR, SEA, LAND)
     * @return Vehicle instance of the appropriate subclass
     */
    public Vehicle createVehicle(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        switch (type.toUpperCase()) {
            case "AIR":
                return new AirVehicle();
            case "SEA":
                return new SeaVehicle();
            case "LAND":
                return new LandVehicle();
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }

    /**
     * Creates a fully configured Vehicle
     */
    public Vehicle createVehicle(String type, String name, String licensePlate,
                                 Double capacity) {
        Vehicle vehicle = createVehicle(type);
        vehicle.setName(name);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setCapacity(capacity);
        return vehicle;
    }
}
