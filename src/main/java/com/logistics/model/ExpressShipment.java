package com.logistics.model;

/**
 * Express Shipment - High priority, fast delivery
 * Demonstrates Liskov Substitution Principle (LSP)
 */
public class ExpressShipment extends Shipment {
    private boolean isFragile;
    private static final Double EXPRESS_RATE = 15.0; // per kg
    private static final Integer EXPRESS_DELIVERY_DAYS = 2;

    public ExpressShipment() {
        super();
        setPriority("HIGH");
    }

    public ExpressShipment(Integer id, String name, String trackingNumber, String senderName,
                           String recipientName, String origin, String destination,
                           Double weight, boolean isFragile) {
        super(id, name, trackingNumber, senderName, recipientName, origin, destination, weight);
        this.isFragile = isFragile;
        setPriority("HIGH");
    }

    @Override
    public String getEntityType() {
        return "EXPRESS_SHIPMENT";
    }

    @Override
    public Double calculateShippingCost() {
        Double baseCost = getWeight() * EXPRESS_RATE;
        return isFragile ? baseCost * 1.5 : baseCost;
    }

    @Override
    public Integer getEstimatedDeliveryDays() {
        return EXPRESS_DELIVERY_DAYS;
    }

    @Override
    public boolean validate() {
        return super.validate() && getPriority().equals("HIGH");
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }
}