# Postman Testing Examples

## Creating Shipments

### 1. Create Express Shipment
**POST** `http://localhost:8080/api/shipments`

Body (raw JSON):
```json
{
  "shipmentType": "EXPRESS",
  "trackingNumber": "SHP-2024-100",
  "senderName": "ABC Technologies",
  "recipientName": "XYZ Retail",
  "origin": "Almaty",
  "destination": "Astana",
  "weight": 125.5,
  "status": "PENDING",
  "priority": "HIGH",
  "isFragile": true,
  "vehicleId": 1,
  "warehouseId": 1
}
```

### 2. Create Standard Shipment with Temperature Control
**POST** `http://localhost:8080/api/shipments`

```json
{
  "shipmentType": "STANDARD",
  "trackingNumber": "SHP-2024-101",
  "senderName": "Food Distributors Inc",
  "recipientName": "Supermarket Chain",
  "origin": "Moscow",
  "destination": "Almaty",
  "weight": 1500.0,
  "temperatureControlled": true
}
```

### 3. Create Economy Shipment
**POST** `http://localhost:8080/api/shipments`

```json
{
  "shipmentType": "ECONOMY",
  "trackingNumber": "SHP-2024-102",
  "senderName": "Wholesale Company",
  "recipientName": "Local Store",
  "origin": "Beijing",
  "destination": "Astana",
  "weight": 5000.0,
  "customsCleared": false
}
```

## Retrieving Shipments

### Get All Shipments
**GET** `http://localhost:8080/api/shipments`

### Get Shipment by ID
**GET** `http://localhost:8080/api/shipments/1`

### Get Shipment by Tracking Number
**GET** `http://localhost:8080/api/shipments/tracking/SHP-2024-001`

### Get Shipments by Status
**GET** `http://localhost:8080/api/shipments/status/PENDING`

## Updating Shipments

### Update Shipment Status
**PUT** `http://localhost:8080/api/shipments/1`

```json
{
  "shipmentType": "EXPRESS",
  "trackingNumber": "SHP-2024-001",
  "senderName": "TechCorp Ltd",
  "recipientName": "Gadget Store",
  "origin": "Astana",
  "destination": "Almaty",
  "weight": 150.5,
  "status": "DELIVERED",
  "priority": "HIGH",
  "isFragile": true
}
```

## Vehicles

### Create Air Vehicle
**POST** `http://localhost:8080/api/vehicles`

```json
{
  "vehicleType": "AIR",
  "name": "Boeing 747 Cargo",
  "licensePlate": "KZ-AIR-100",
  "capacity": 35000.0,
  "status": "AVAILABLE",
  "maxAltitude": 13500
}
```

### Create Land Vehicle
**POST** `http://localhost:8080/api/vehicles`

```json
{
  "vehicleType": "LAND",
  "name": "Scania R500",
  "licensePlate": "KZ-10-TRK-555",
  "capacity": 20000.0,
  "status": "AVAILABLE",
  "fuelType": "DIESEL"
}
```

### Create Sea Vehicle
**POST** `http://localhost:8080/api/vehicles`

```json
{
  "vehicleType": "SEA",
  "name": "Container Ship Atlantic",
  "licensePlate": "KZ-SEA-050",
  "capacity": 60000.0,
  "status": "IN_TRANSIT",
  "cargoType": "CONTAINER"
}
```

## Warehouses

### Create Warehouse
**POST** `http://localhost:8080/api/warehouses`

```json
{
  "name": "East Distribution Center",
  "location": "Shymkent, Kazakhstan",
  "capacity": 8000,
  "currentLoad": 1200
}
```

### Get All Warehouses
**GET** `http://localhost:8080/api/warehouses`

## Testing Error Scenarios

### 1. Duplicate Tracking Number
**POST** `http://localhost:8080/api/shipments`

Use an existing tracking number to trigger 409 Conflict

### 2. Invalid Weight
**POST** `http://localhost:8080/api/shipments`

```json
{
  "shipmentType": "EXPRESS",
  "trackingNumber": "SHP-2024-999",
  "senderName": "Test",
  "recipientName": "Test",
  "weight": -10.0
}
```

Expected: 400 Bad Request

### 3. Non-existent Resource
**GET** `http://localhost:8080/api/shipments/99999`

Expected: 404 Not Found

## Expected Responses

### Success (201 Created)
```json
{
  "id": 5,
  "shipmentType": "EXPRESS",
  "trackingNumber": "SHP-2024-100",
  "senderName": "ABC Technologies",
  "recipientName": "XYZ Retail",
  "origin": "Almaty",
  "destination": "Astana",
  "weight": 125.5,
  "status": "PENDING",
  "priority": "HIGH",
  "estimatedDelivery": null,
  "vehicleId": 1,
  "warehouseId": 1,
  "isFragile": true,
  "temperatureControlled": null,
  "customsCleared": null
}
```

### Error (404 Not Found)
```json
{
  "timestamp": "2024-02-07T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Shipment not found with id: 99999",
  "path": "/api/shipments/99999"
}
```
