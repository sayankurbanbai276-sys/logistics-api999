package com.logistics.service;

import com.logistics.dto.WarehouseDTO;
import com.logistics.exception.InvalidInputException;
import com.logistics.model.Warehouse;
import com.logistics.patterns.singleton.LoggingService;
import com.logistics.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final WarehouseRepository repository;
    private final LoggingService loggingService;

    @Autowired
    public WarehouseService(WarehouseRepository repository) {
        this.repository = repository;
        this.loggingService = LoggingService.getInstance();
    }

    public WarehouseDTO createWarehouse(WarehouseDTO dto) {
        validateWarehouseDTO(dto);

        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.getName());
        warehouse.setLocation(dto.getLocation());
        warehouse.setCapacity(dto.getCapacity());
        warehouse.setCurrentLoad(dto.getCurrentLoad() != null ? dto.getCurrentLoad() : 0);

        Warehouse created = repository.create(warehouse);
        return convertToDTO(created);
    }

    public List<WarehouseDTO> getAllWarehouses() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public WarehouseDTO getWarehouseById(Integer id) {
        return convertToDTO(repository.findById(id));
    }

    public WarehouseDTO updateWarehouse(Integer id, WarehouseDTO dto) {
        validateWarehouseDTO(dto);

        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.getName());
        warehouse.setLocation(dto.getLocation());
        warehouse.setCapacity(dto.getCapacity());
        warehouse.setCurrentLoad(dto.getCurrentLoad());

        Warehouse updated = repository.update(id, warehouse);
        return convertToDTO(updated);
    }

    public void deleteWarehouse(Integer id) {
        repository.delete(id);
    }

    private void validateWarehouseDTO(WarehouseDTO dto) {
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new InvalidInputException("Name is required");
        }
        if (dto.getLocation() == null || dto.getLocation().isEmpty()) {
            throw new InvalidInputException("Location is required");
        }
        if (dto.getCapacity() == null || dto.getCapacity() <= 0) {
            throw new InvalidInputException("Capacity must be positive");
        }
    }

    private WarehouseDTO convertToDTO(Warehouse warehouse) {
        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(warehouse.getId());
        dto.setName(warehouse.getName());
        dto.setLocation(warehouse.getLocation());
        dto.setCapacity(warehouse.getCapacity());
        dto.setCurrentLoad(warehouse.getCurrentLoad());
        return dto;
    }
}
