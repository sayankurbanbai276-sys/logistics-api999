package com.logistics.service;

import com.logistics.dto.ShipmentDTO;
import com.logistics.exception.DuplicateResourceException;
import com.logistics.exception.InvalidInputException;
import com.logistics.model.Shipment;
import com.logistics.patterns.builder.ShipmentBuilder;
import com.logistics.patterns.singleton.LoggingService;
import com.logistics.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Shipment business logic
 * Demonstrates Single Responsibility Principle (SRP)
 */
@Service
public class ShipmentService {

    private final ShipmentRepository repository;
    private final LoggingService loggingService;

    @Autowired
    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
        this.loggingService = LoggingService.getInstance();
    }

    public ShipmentDTO createShipment(ShipmentDTO dto) {
        validateShipmentDTO(dto);

        loggingService.info("Creating new shipment: " + dto.getTrackingNumber());

        try {
            // Use Builder Pattern to construct Shipment
            ShipmentBuilder builder = new ShipmentBuilder()
                    .type(dto.getShipmentType())
                    .trackingNumber(dto.getTrackingNumber())
                    .sender(dto.getSenderName())
                    .recipient(dto.getRecipientName())
                    .from(dto.getOrigin())
                    .to(dto.getDestination())
                    .weight(dto.getWeight());

            if (dto.getStatus() != null) {
                builder.status(dto.getStatus());
            }
            if (dto.getPriority() != null) {
                builder.priority(dto.getPriority());
            }
            if (dto.getEstimatedDelivery() != null) {
                builder.estimatedDelivery(dto.getEstimatedDelivery());
            }
            if (dto.getVehicleId() != null) {
                builder.vehicleId(dto.getVehicleId());
            }
            if (dto.getWarehouseId() != null) {
                builder.warehouseId(dto.getWarehouseId());
            }

            // Handle special attributes based on shipment type
            if (dto.getIsFragile() != null) {
                builder.specialAttribute(dto.getIsFragile());
            } else if (dto.getTemperatureControlled() != null) {
                builder.specialAttribute(dto.getTemperatureControlled());
            } else if (dto.getCustomsCleared() != null) {
                builder.specialAttribute(dto.getCustomsCleared());
            }

            Shipment shipment = builder.build();
            Shipment created = repository.create(shipment);

            loggingService.info("Shipment created successfully with ID: " + created.getId());
            return convertToDTO(created);

        } catch (DuplicateKeyException e) {
            throw new DuplicateResourceException("Shipment with tracking number already exists: " + dto.getTrackingNumber());
        }
    }

    public List<ShipmentDTO> getAllShipments() {
        loggingService.info("Fetching all shipments");
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShipmentDTO getShipmentById(Integer id) {
        loggingService.info("Fetching shipment with ID: " + id);
        Shipment shipment = repository.findById(id);
        return convertToDTO(shipment);
    }

    public ShipmentDTO getShipmentByTrackingNumber(String trackingNumber) {
        loggingService.info("Fetching shipment with tracking number: " + trackingNumber);
        Shipment shipment = repository.findByTrackingNumber(trackingNumber);
        return convertToDTO(shipment);
    }

    public ShipmentDTO updateShipment(Integer id, ShipmentDTO dto) {
        validateShipmentDTO(dto);
        loggingService.info("Updating shipment ID: " + id);

        ShipmentBuilder builder = new ShipmentBuilder()
                .type(dto.getShipmentType())
                .trackingNumber(dto.getTrackingNumber())
                .sender(dto.getSenderName())
                .recipient(dto.getRecipientName())
                .from(dto.getOrigin())
                .to(dto.getDestination())
                .weight(dto.getWeight())
                .status(dto.getStatus())
                .priority(dto.getPriority());

        if (dto.getEstimatedDelivery() != null) {
            builder.estimatedDelivery(dto.getEstimatedDelivery());
        }
        if (dto.getVehicleId() != null) {
            builder.vehicleId(dto.getVehicleId());
        }
        if (dto.getWarehouseId() != null) {
            builder.warehouseId(dto.getWarehouseId());
        }

        if (dto.getIsFragile() != null) {
            builder.specialAttribute(dto.getIsFragile());
        } else if (dto.getTemperatureControlled() != null) {
            builder.specialAttribute(dto.getTemperatureControlled());
        } else if (dto.getCustomsCleared() != null) {
            builder.specialAttribute(dto.getCustomsCleared());
        }

        Shipment shipment = builder.build();
        Shipment updated = repository.update(id, shipment);

        loggingService.info("Shipment updated successfully: " + id);
        return convertToDTO(updated);
    }

    public void deleteShipment(Integer id) {
        loggingService.info("Deleting shipment ID: " + id);
        repository.delete(id);
        loggingService.info("Shipment deleted successfully: " + id);
    }

    public List<ShipmentDTO> getShipmentsByStatus(String status) {
        loggingService.info("Fetching shipments with status: " + status);
        return repository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateShipmentDTO(ShipmentDTO dto) {
        if (dto.getShipmentType() == null || dto.getShipmentType().isEmpty()) {
            throw new InvalidInputException("Shipment type is required");
        }
        if (dto.getTrackingNumber() == null || dto.getTrackingNumber().isEmpty()) {
            throw new InvalidInputException("Tracking number is required");
        }
        if (dto.getSenderName() == null || dto.getSenderName().isEmpty()) {
            throw new InvalidInputException("Sender name is required");
        }
        if (dto.getRecipientName() == null || dto.getRecipientName().isEmpty()) {
            throw new InvalidInputException("Recipient name is required");
        }
        if (dto.getWeight() == null || dto.getWeight() <= 0) {
            throw new InvalidInputException("Weight must be positive");
        }
    }

    private ShipmentDTO convertToDTO(Shipment shipment) {
        ShipmentDTO dto = new ShipmentDTO();
        dto.setId(shipment.getId());
        dto.setShipmentType(shipment.getEntityType().replace("_SHIPMENT", ""));
        dto.setTrackingNumber(shipment.getTrackingNumber());
        dto.setSenderName(shipment.getSenderName());
        dto.setRecipientName(shipment.getRecipientName());
        dto.setOrigin(shipment.getOrigin());
        dto.setDestination(shipment.getDestination());
        dto.setWeight(shipment.getWeight());
        dto.setStatus(shipment.getStatus());
        dto.setPriority(shipment.getPriority());
        dto.setEstimatedDelivery(shipment.getEstimatedDelivery());
        dto.setVehicleId(shipment.getVehicleId());
        dto.setWarehouseId(shipment.getWarehouseId());

        // Handle type-specific attributes
        if (shipment instanceof com.logistics.model.ExpressShipment) {
            dto.setIsFragile(((com.logistics.model.ExpressShipment) shipment).isFragile());
        } else if (shipment instanceof com.logistics.model.StandardShipment) {
            dto.setTemperatureControlled(((com.logistics.model.StandardShipment) shipment).isTemperatureControlled());
        } else if (shipment instanceof com.logistics.model.EconomyShipment) {
            dto.setCustomsCleared(((com.logistics.model.EconomyShipment) shipment).isCustomsCleared());
        }

        return dto;
    }
}
