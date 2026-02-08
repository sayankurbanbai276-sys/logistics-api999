package com.logistics.repository;

import com.logistics.exception.DatabaseOperationException;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.model.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseRepository {

    private final DataSource dataSource;

    @Autowired
    public WarehouseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Warehouse create(Warehouse warehouse) {
        String sql = "INSERT INTO warehouses (name, location, capacity, current_load) VALUES (?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, warehouse.getName());
            stmt.setString(2, warehouse.getLocation());
            stmt.setInt(3, warehouse.getCapacity());
            stmt.setInt(4, warehouse.getCurrentLoad());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    warehouse.setId(keys.getInt(1));
                }
            }
            return warehouse;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating warehouse: " + e.getMessage(), e);
        }
    }

    public List<Warehouse> findAll() {
        String sql = "SELECT * FROM warehouses ORDER BY id";
        List<Warehouse> warehouses = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                warehouses.add(mapResultSetToWarehouse(rs));
            }
            return warehouses;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error fetching warehouses: " + e.getMessage(), e);
        }
    }

    public Warehouse findById(Integer id) {
        String sql = "SELECT * FROM warehouses WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWarehouse(rs);
                }
                throw new ResourceNotFoundException("Warehouse not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error finding warehouse: " + e.getMessage(), e);
        }
    }

    public Warehouse update(Integer id, Warehouse warehouse) {
        String sql = "UPDATE warehouses SET name = ?, location = ?, capacity = ?, current_load = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, warehouse.getName());
            stmt.setString(2, warehouse.getLocation());
            stmt.setInt(3, warehouse.getCapacity());
            stmt.setInt(4, warehouse.getCurrentLoad());
            stmt.setInt(5, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Warehouse not found with id: " + id);
            }
            warehouse.setId(id);
            return warehouse;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating warehouse: " + e.getMessage(), e);
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM warehouses WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new ResourceNotFoundException("Warehouse not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting warehouse: " + e.getMessage(), e);
        }
    }

    private Warehouse mapResultSetToWarehouse(ResultSet rs) throws SQLException {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(rs.getInt("id"));
        warehouse.setName(rs.getString("name"));
        warehouse.setLocation(rs.getString("location"));
        warehouse.setCapacity(rs.getInt("capacity"));
        warehouse.setCurrentLoad(rs.getInt("current_load"));
        return warehouse;
    }
}
