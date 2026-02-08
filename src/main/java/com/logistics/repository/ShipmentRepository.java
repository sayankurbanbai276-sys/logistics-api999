package com.logistics.repository;

import com.logistics.exception.DatabaseOperationException;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.model.*;
import com.logistics.patterns.factory.ShipmentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Shipment CRUD operations
 * Demonstrates Dependency Inversion Principle (DIP)
 */
@Repository
public class ShipmentRepository {

    private final DataSource dataSource;
    private final ShipmentFactory shipmentFactory;

    @Autowired
    public ShipmentRepository(DataSource dataSource, ShipmentFactory shipmentFactory) {
        this.dataSource = dataSource;
        this.shipmentFactory = shipmentFactory;
    }

    public Shipment create(Shipment shipment) {
        String sql = "INSERT INTO shipments (tracking_number, shipment_type, sender_name, " +
                "recipient_name, origin, destination, weight, status, priority, " +
                "estimated_delivery, vehicle_id, warehouse_id, is_fragile, " +
                "temperature_controlled, customs_cleared) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setShipmentParameters(stmt, shipment);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating shipment failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    shipment.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseOperationException("Creating shipment failed, no ID obtained");
                }
            }

            return shipment;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating shipment: " + e.getMessage(), e);
        }
    }

    public List<Shipment> findAll() {
        String sql = "SELECT * FROM shipments ORDER BY id";
        List<Shipment> shipments = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                shipments.add(mapResultSetToShipment(rs));
            }

            return shipments;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching shipments: " + e.getMessage(), e);
        }
    }

    public Shipment findById(Integer id) {
        String sql = "SELECT * FROM shipments WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToShipment(rs);
                } else {
                    throw new ResourceNotFoundException("Shipment not found with id: " + id);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding shipment: " + e.getMessage(), e);
        }
    }

    public Shipment findByTrackingNumber(String trackingNumber) {
        String sql = "SELECT * FROM shipments WHERE tracking_number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trackingNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToShipment(rs);
                } else {
                    throw new ResourceNotFoundException("Shipment not found with tracking number: " + trackingNumber);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding shipment: " + e.getMessage(), e);
        }
    }

    public Shipment update(Integer id, Shipment shipment) {
        String sql = "UPDATE shipments SET tracking_number = ?, shipment_type = ?, " +
                "sender_name = ?, recipient_name = ?, origin = ?, destination = ?, " +
                "weight = ?, status = ?, priority = ?, estimated_delivery = ?, " +
                "vehicle_id = ?, warehouse_id = ?, is_fragile = ?, " +
                "temperature_controlled = ?, customs_cleared = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setShipmentParameters(stmt, shipment);
            stmt.setInt(16, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Shipment not found with id: " + id);
            }

            shipment.setId(id);
            return shipment;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating shipment: " + e.getMessage(), e);
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM shipments WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Shipment not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting shipment: " + e.getMessage(), e);
        }
    }

    public List<Shipment> findByStatus(String status) {
        String sql = "SELECT * FROM shipments WHERE status = ? ORDER BY id";
        List<Shipment> shipments = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shipments.add(mapResultSetToShipment(rs));
                }
            }

            return shipments;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding shipments by status: " + e.getMessage(), e);
        }
    }

    private void setShipmentParameters(PreparedStatement stmt, Shipment shipment) throws SQLException {
        stmt.setString(1, shipment.getTrackingNumber());
        stmt.setString(2, shipment.getEntityType().replace("_SHIPMENT", ""));
        stmt.setString(3, shipment.getSenderName());
        stmt.setString(4, shipment.getRecipientName());
        stmt.setString(5, shipment.getOrigin());
        stmt.setString(6, shipment.getDestination());
        stmt.setDouble(7, shipment.getWeight());
        stmt.setString(8, shipment.getStatus());
        stmt.setString(9, shipment.getPriority());

        if (shipment.getEstimatedDelivery() != null) {
            stmt.setDate(10, Date.valueOf(shipment.getEstimatedDelivery()));
        } else {
            stmt.setNull(10, Types.DATE);
        }

        if (shipment.getVehicleId() != null) {
            stmt.setInt(11, shipment.getVehicleId());
        } else {
            stmt.setNull(11, Types.INTEGER);
        }

        if (shipment.getWarehouseId() != null) {
            stmt.setInt(12, shipment.getWarehouseId());
        } else {
            stmt.setNull(12, Types.INTEGER);
        }

        // Type-specific attributes
        if (shipment instanceof ExpressShipment) {
            stmt.setBoolean(13, ((ExpressShipment) shipment).isFragile());
            stmt.setBoolean(14, false);
            stmt.setBoolean(15, false);
        } else if (shipment instanceof StandardShipment) {
            stmt.setBoolean(13, false);
            stmt.setBoolean(14, ((StandardShipment) shipment).isTemperatureControlled());
            stmt.setBoolean(15, false);
        } else if (shipment instanceof EconomyShipment) {
            stmt.setBoolean(13, false);
            stmt.setBoolean(14, false);
            stmt.setBoolean(15, ((EconomyShipment) shipment).isCustomsCleared());
        } else {
            stmt.setBoolean(13, false);
            stmt.setBoolean(14, false);
            stmt.setBoolean(15, false);
        }
    }

    private Shipment mapResultSetToShipment(ResultSet rs) throws SQLException {
        String type = rs.getString("shipment_type");
        Shipment shipment = shipmentFactory.createShipment(type);

        shipment.setId(rs.getInt("id"));
        shipment.setTrackingNumber(rs.getString("tracking_number"));
        shipment.setSenderName(rs.getString("sender_name"));
        shipment.setRecipientName(rs.getString("recipient_name"));
        shipment.setOrigin(rs.getString("origin"));
        shipment.setDestination(rs.getString("destination"));
        shipment.setWeight(rs.getDouble("weight"));
        shipment.setStatus(rs.getString("status"));
        shipment.setPriority(rs.getString("priority"));

        Date estimatedDelivery = rs.getDate("estimated_delivery");
        if (estimatedDelivery != null) {
            shipment.setEstimatedDelivery(estimatedDelivery.toLocalDate());
        }

        shipment.setVehicleId((Integer) rs.getObject("vehicle_id"));
        shipment.setWarehouseId((Integer) rs.getObject("warehouse_id"));

        // Set type-specific attributes
        if (shipment instanceof ExpressShipment) {
            ((ExpressShipment) shipment).setFragile(rs.getBoolean("is_fragile"));
        } else if (shipment instanceof StandardShipment) {
            ((StandardShipment) shipment).setTemperatureControlled(rs.getBoolean("temperature_controlled"));
        } else if (shipment instanceof EconomyShipment) {
            ((EconomyShipment) shipment).setCustomsCleared(rs.getBoolean("customs_cleared"));
        }

        return shipment;
    }
}
