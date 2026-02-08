package com.logistics.service;

import com.logistics.dto.VehicleDTO;
import com.logistics.exception.InvalidInputException;
import com.logistics.model.*;
import com.logistics.patterns.factory.VehicleFactory;
import com.logistics.patterns.singleton.LoggingService;
import com.logistics.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository repository;
    private final VehicleFactory factory;
    private final LoggingService loggingService;

    @Autowired
    public VehicleService(VehicleRepository repository, VehicleFactory factory) {
        this.repository = repository;
        this.factory = factory;
        this.loggingService = LoggingService.getInstance();
    }

    public VehicleDTO createVehicle(VehicleDTO dto) {
        validateVehicleDTO(dto);
        loggingService.info("Creating vehicle: " + dto.getName());

        Vehicle vehicle = factory.createVehicle(dto.getVehicleType());
        vehicle.setName(dto.getName());
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setStatus(dto.getStatus() != null ? dto.getStatus() : "AVAILABLE");

        if (vehicle instanceof AirVehicle && dto.getMaxAltitude() != null) {
            ((AirVehicle) vehicle).setMaxAltitude(dto.getMaxAltitude());
        } else if (vehicle instanceof SeaVehicle && dto.getCargoType() != null) {
            ((SeaVehicle) vehicle).setCargoType(dto.getCargoType());
        } else if (vehicle instanceof LandVehicle && dto.getFuelType() != null) {
            ((LandVehicle) vehicle).setFuelType(dto.getFuelType());
        }

        Vehicle created = repository.create(vehicle);
        return convertToDTO(created);
    }

    public List<VehicleDTO> getAllVehicles() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Integer id) {
        return convertToDTO(repository.findById(id));
    }

    public VehicleDTO updateVehicle(Integer id, VehicleDTO dto) {
        validateVehicleDTO(dto);

        Vehicle vehicle = factory.createVehicle(dto.getVehicleType());
        vehicle.setName(dto.getName());
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setStatus(dto.getStatus());

        if (vehicle instanceof AirVehicle && dto.getMaxAltitude() != null) {
            ((AirVehicle) vehicle).setMaxAltitude(dto.getMaxAltitude());
        } else if (vehicle instanceof SeaVehicle && dto.getCargoType() != null) {
            ((SeaVehicle) vehicle).setCargoType(dto.getCargoType());
        } else if (vehicle instanceof LandVehicle && dto.getFuelType() != null) {
            ((LandVehicle) vehicle).setFuelType(dto.getFuelType());
        }

        Vehicle updated = repository.update(id, vehicle);
        return convertToDTO(updated);
    }

    public void deleteVehicle(Integer id) {
        repository.delete(id);
    }

    private void validateVehicleDTO(VehicleDTO dto) {
        if (dto.getVehicleType() == null) {
            throw new InvalidInputException("Vehicle type is required");
        }
        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new InvalidInputException("Name is required");
        }
        if (dto.getLicensePlate() == null || dto.getLicensePlate().isEmpty()) {
            throw new InvalidInputException("License plate is required");
        }
        if (dto.getCapacity() == null || dto.getCapacity() <= 0) {
            throw new InvalidInputException("Capacity must be positive");
        }
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setVehicleType(vehicle.getVehicleType());
        dto.setName(vehicle.getName());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setCapacity(vehicle.getCapacity());
        dto.setStatus(vehicle.getStatus());

        if (vehicle instanceof AirVehicle) {
            dto.setMaxAltitude(((AirVehicle) vehicle).getMaxAltitude());
        } else if (vehicle instanceof SeaVehicle) {
            dto.setCargoType(((SeaVehicle) vehicle).getCargoType());
        } else if (vehicle instanceof LandVehicle) {
            dto.setFuelType(((LandVehicle) vehicle).getFuelType());
        }

        return dto;
    }
}
