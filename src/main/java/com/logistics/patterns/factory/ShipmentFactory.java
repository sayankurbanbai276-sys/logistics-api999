package com.logistics.patterns.factory;

import com.logistics.model.*;
import org.springframework.stereotype.Component;

/**
 * FACTORY PATTERN - Shipment Factory
 *
 * Purpose: Creates different types of Shipment objects based on type
 * Benefits:
 * - Encapsulates object creation logic
 * - Easy to add new shipment types (Open-Closed Principle)
 * - Client code doesn't need to know concrete classes
 */
@Component
public class ShipmentFactory {

    /**
     * Creates a Shipment object based on the specified type
     *
     * @param type The shipment type (EXPRESS, STANDARD, ECONOMY)
     * @return Shipment instance of the appropriate subclass
     * @throws IllegalArgumentException if type is invalid
     */
    public Shipment createShipment(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Shipment type cannot be null");
        }

        switch (type.toUpperCase()) {
            case "EXPRESS":
                return new ExpressShipment();
            case "STANDARD":
                return new StandardShipment();
            case "ECONOMY":
                return new EconomyShipment();
            default:
                throw new IllegalArgumentException("Unknown shipment type: " + type);
        }
    }

    /**
     * Creates a fully configured Shipment
     */
    public Shipment createShipment(String type, String trackingNumber, String senderName,
                                   String recipientName, String origin, String destination,
                                   Double weight) {
        Shipment shipment = createShipment(type);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setSenderName(senderName);
        shipment.setRecipientName(recipientName);
        shipment.setOrigin(origin);
        shipment.setDestination(destination);
        shipment.setWeight(weight);
        return shipment;
    }

    /**
     * Creates a Shipment with all specific attributes
     */
    public Shipment createShipmentWithAttributes(String type, String trackingNumber,
                                                 String senderName, String recipientName,
                                                 String origin, String destination,
                                                 Double weight, boolean specialAttribute) {
        Shipment shipment = createShipment(type);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setSenderName(senderName);
        shipment.setRecipientName(recipientName);
        shipment.setOrigin(origin);
        shipment.setDestination(destination);
        shipment.setWeight(weight);

        // Set type-specific attributes
        if (shipment instanceof ExpressShipment) {
            ((ExpressShipment) shipment).setFragile(specialAttribute);
        } else if (shipment instanceof StandardShipment) {
            ((StandardShipment) shipment).setTemperatureControlled(specialAttribute);
        } else if (shipment instanceof EconomyShipment) {
            ((EconomyShipment) shipment).setCustomsCleared(specialAttribute);
        }

        return shipment;
    }
}
