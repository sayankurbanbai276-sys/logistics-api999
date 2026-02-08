package com.logistics.model;

/**
 * Standard Shipment - Normal priority, regular delivery
 */
public class StandardShipment extends Shipment {
    private boolean temperatureControlled;
    private static final Double STANDARD_RATE = 8.0; // per kg
    private static final Integer STANDARD_DELIVERY_DAYS = 5;

    public StandardShipment() {
        super();
        setPriority("NORMAL");
    }

    public StandardShipment(Integer id, String name, String trackingNumber, String senderName,
                            String recipientName, String origin, String destination,
                            Double weight, boolean temperatureControlled) {
        super(id, name, trackingNumber, senderName, recipientName, origin, destination, weight);
        this.temperatureControlled = temperatureControlled;
        setPriority("NORMAL");
    }

    @Override
    public String getEntityType() {
        return "STANDARD_SHIPMENT";
    }

    @Override
    public Double calculateShippingCost() {
        Double baseCost = getWeight() * STANDARD_RATE;
        return temperatureControlled ? baseCost * 1.3 : baseCost;
    }

    @Override
    public Integer getEstimatedDeliveryDays() {
        return STANDARD_DELIVERY_DAYS;
    }

    public boolean isTemperatureControlled() {
        return temperatureControlled;
    }

    public void setTemperatureControlled(boolean temperatureControlled) {
        this.temperatureControlled = temperatureControlled;
    }
}