package com.logistics.model;

/**
 * Economy Shipment - Low priority, budget delivery
 */
public class EconomyShipment extends Shipment {
    private boolean customsCleared;
    private static final Double ECONOMY_RATE = 5.0; // per kg
    private static final Integer ECONOMY_DELIVERY_DAYS = 10;

    public EconomyShipment() {
        super();
        setPriority("LOW");
    }

    public EconomyShipment(Integer id, String name, String trackingNumber, String senderName,
                           String recipientName, String origin, String destination,
                           Double weight, boolean customsCleared) {
        super(id, name, trackingNumber, senderName, recipientName, origin, destination, weight);
        this.customsCleared = customsCleared;
        setPriority("LOW");
    }

    @Override
    public String getEntityType() {
        return "ECONOMY_SHIPMENT";
    }

    @Override
    public Double calculateShippingCost() {
        Double baseCost = getWeight() * ECONOMY_RATE;
        return customsCleared ? baseCost : baseCost * 0.9;
    }

    @Override
    public Integer getEstimatedDeliveryDays() {
        return customsCleared ? ECONOMY_DELIVERY_DAYS : ECONOMY_DELIVERY_DAYS + 3;
    }

    public boolean isCustomsCleared() {
        return customsCleared;
    }

    public void setCustomsCleared(boolean customsCleared) {
        this.customsCleared = customsCleared;
    }
}