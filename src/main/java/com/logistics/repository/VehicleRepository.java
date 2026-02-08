package com.logistics.repository;

import com.logistics.exception.DatabaseOperationException;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.model.*;
import com.logistics.patterns.factory.VehicleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VehicleRepository {

    private final DataSource dataSource;
    private final VehicleFactory vehicleFactory;

    @Autowired
    public VehicleRepository(DataSource dataSource, VehicleFactory vehicleFactory) {
        this.dataSource = dataSource;
        this.vehicleFactory = vehicleFactory;
    }

    public Vehicle create(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (vehicle_type, name, license_plate, capacity, status, " +
                "max_altitude, cargo_type, fuel_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setVehicleParameters(stmt, vehicle);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    vehicle.setId(keys.getInt(1));
                }
            }
            return vehicle;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating vehicle: " + e.getMessage(), e);
        }
    }

    public List<Vehicle> findAll() {
        String sql = "SELECT * FROM vehicles ORDER BY id";
        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
            return vehicles;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching vehicles: " + e.getMessage(), e);
        }
    }

    public Vehicle findById(Integer id) {
        String sql = "SELECT * FROM vehicles WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicle(rs);
                }
                throw new ResourceNotFoundException("Vehicle not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding vehicle: " + e.getMessage(), e);
        }
    }

    public Vehicle update(Integer id, Vehicle vehicle) {
        String sql = "UPDATE vehicles SET vehicle_type = ?, name = ?, license_plate = ?, " +
                "capacity = ?, status = ?, max_altitude = ?, cargo_type = ?, fuel_type = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setVehicleParameters(stmt, vehicle);
            stmt.setInt(9, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Vehicle not found with id: " + id);
            }
            vehicle.setId(id);
            return vehicle;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating vehicle: " + e.getMessage(), e);
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM vehicles WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Vehicle not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting vehicle: " + e.getMessage(), e);
        }
    }

    private void setVehicleParameters(PreparedStatement stmt, Vehicle vehicle) throws SQLException {
        stmt.setString(1, vehicle.getVehicleType());
        stmt.setString(2, vehicle.getName());
        stmt.setString(3, vehicle.getLicensePlate());
        stmt.setDouble(4, vehicle.getCapacity());
        stmt.setString(5, vehicle.getStatus());

        if (vehicle instanceof AirVehicle) {
            stmt.setObject(6, ((AirVehicle) vehicle).getMaxAltitude());
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.VARCHAR);
        } else if (vehicle instanceof SeaVehicle) {
            stmt.setNull(6, Types.INTEGER);
            stmt.setString(7, ((SeaVehicle) vehicle).getCargoType());
            stmt.setNull(8, Types.VARCHAR);
        } else if (vehicle instanceof LandVehicle) {
            stmt.setNull(6, Types.INTEGER);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setString(8, ((LandVehicle) vehicle).getFuelType());
        }
    }

    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        String type = rs.getString("vehicle_type");
        Vehicle vehicle = vehicleFactory.createVehicle(type);

        vehicle.setId(rs.getInt("id"));
        vehicle.setName(rs.getString("name"));
        vehicle.setLicensePlate(rs.getString("license_plate"));
        vehicle.setCapacity(rs.getDouble("capacity"));
        vehicle.setStatus(rs.getString("status"));

        if (vehicle instanceof AirVehicle) {
            ((AirVehicle) vehicle).setMaxAltitude((Integer) rs.getObject("max_altitude"));
        } else if (vehicle instanceof SeaVehicle) {
            ((SeaVehicle) vehicle).setCargoType(rs.getString("cargo_type"));
        } else if (vehicle instanceof LandVehicle) {
            ((LandVehicle) vehicle).setFuelType(rs.getString("fuel_type"));
        }

        return vehicle;
    }
}
