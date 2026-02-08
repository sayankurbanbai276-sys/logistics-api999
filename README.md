# Smart Logistics Management System - REST API

> A comprehensive logistics management system built with Spring Boot, implementing design patterns, SOLID principles, and RESTful API architecture.

## 📋 Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [REST API Documentation](#rest-api-documentation)
- [Design Patterns](#design-patterns)
- [Component Principles](#component-principles)
- [SOLID & OOP](#solid--oop)
- [Database Schema](#database-schema)
- [System Architecture](#system-architecture)
- [Installation & Setup](#installation--setup)
- [API Testing](#api-testing)
- [Reflection](#reflection)

---

## 🎯 Project Overview

The **Smart Logistics Management System** is a RESTful API built to manage shipments, vehicles, and warehouses in a logistics operation. This project demonstrates professional backend development practices including:

- **Spring Boot** framework for enterprise-grade APIs
- **Design Patterns** (Singleton, Factory, Builder)
- **Component Principles** (REP, CCP, CRP)
- **SOLID Principles** throughout the architecture
- **Advanced OOP** features (abstraction, inheritance, polymorphism)
- **JDBC** for database operations
- **Exception handling** with global error responses

### Domain Model

The system manages three main entities:
- **Shipments**: Express, Standard, and Economy types
- **Vehicles**: Air, Sea, and Land transport
- **Warehouses**: Storage facilities with capacity management

---

## ✨ Features

- ✅ Full CRUD operations for Shipments, Vehicles, and Warehouses
- ✅ RESTful API with JSON request/response
- ✅ Polymorphic shipment types with type-specific attributes
- ✅ Factory pattern for object creation
- ✅ Builder pattern for complex object construction
- ✅ Singleton pattern for configuration and logging
- ✅ Global exception handling
- ✅ Input validation and business rules
- ✅ Database persistence with JDBC
- ✅ Tracking number-based shipment lookup
- ✅ Status-based shipment filtering

---

## 🌐 REST API Documentation

### Base URL
```
http://localhost:8080/api
```

### Shipments Endpoints

#### 1. Get All Shipments
```http
GET /api/shipments
```

**Response:**
```json
[
  {
    "id": 1,
    "shipmentType": "EXPRESS",
    "trackingNumber": "SHP-2024-001",
    "senderName": "TechCorp Ltd",
    "recipientName": "Gadget Store",
    "origin": "Astana",
    "destination": "Almaty",
    "weight": 150.5,
    "status": "IN_TRANSIT",
    "priority": "HIGH",
    "estimatedDelivery": "2024-02-10",
    "vehicleId": 3,
    "warehouseId": 1,
    "isFragile": true
  }
]
```

#### 2. Get Shipment by ID
```http
GET /api/shipments/{id}
```

#### 3. Get Shipment by Tracking Number
```http
GET /api/shipments/tracking/{trackingNumber}
```

**Example:**
```http
GET /api/shipments/tracking/SHP-2024-001
```

#### 4. Get Shipments by Status
```http
GET /api/shipments/status/{status}
```

**Example:**
```http
GET /api/shipments/status/PENDING
```

#### 5. Create Shipment
```http
POST /api/shipments
```

**Request Body (Express Shipment):**
```json
{
  "shipmentType": "EXPRESS",
  "trackingNumber": "SHP-2024-005",
  "senderName": "ABC Company",
  "recipientName": "XYZ Store",
  "origin": "Almaty",
  "destination": "Astana",
  "weight": 200.0,
  "status": "PENDING",
  "priority": "HIGH",
  "isFragile": true
}
```

**Request Body (Standard Shipment):**
```json
{
  "shipmentType": "STANDARD",
  "trackingNumber": "SHP-2024-006",
  "senderName": "Food Corp",
  "recipientName": "Restaurant Chain",
  "origin": "Moscow",
  "destination": "Almaty",
  "weight": 500.0,
  "temperatureControlled": true
}
```

#### 6. Update Shipment
```http
PUT /api/shipments/{id}
```

#### 7. Delete Shipment
```http
DELETE /api/shipments/{id}
```

---

### Vehicles Endpoints

#### 1. Get All Vehicles
```http
GET /api/vehicles
```

**Response:**
```json
[
  {
    "id": 1,
    "vehicleType": "AIR",
    "name": "Cargo Plane A320",
    "licensePlate": "KZ-AIR-001",
    "capacity": 25000.0,
    "status": "AVAILABLE",
    "maxAltitude": 12000
  }
]
```

#### 2. Create Vehicle (Air)
```http
POST /api/vehicles
```

**Request Body:**
```json
{
  "vehicleType": "AIR",
  "name": "Boeing 747 Cargo",
  "licensePlate": "KZ-AIR-005",
  "capacity": 30000.0,
  "status": "AVAILABLE",
  "maxAltitude": 13000
}
```

#### 3. Create Vehicle (Land)
```http
POST /api/vehicles
```

**Request Body:**
```json
{
  "vehicleType": "LAND",
  "name": "Volvo Truck FH16",
  "licensePlate": "KZ-03-ABC-789",
  "capacity": 18000.0,
  "status": "AVAILABLE",
  "fuelType": "DIESEL"
}
```

---

### Warehouses Endpoints

#### 1. Get All Warehouses
```http
GET /api/warehouses
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Central Warehouse",
    "location": "Astana, Kazakhstan",
    "capacity": 10000,
    "currentLoad": 2500
  }
]
```

#### 2. Create Warehouse
```http
POST /api/warehouses
```

**Request Body:**
```json
{
  "name": "North Hub",
  "location": "Petropavlovsk",
  "capacity": 5000,
  "currentLoad": 0
}
```

---

### Error Responses

All errors return a standard JSON format:

```json
{
  "timestamp": "2024-02-07T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Shipment not found with id: 999",
  "path": "/api/shipments/999"
}
```

**HTTP Status Codes:**
- `200` - OK
- `201` - Created
- `204` - No Content (successful deletion)
- `400` - Bad Request (validation error)
- `404` - Not Found
- `409` - Conflict (duplicate resource)
- `500` - Internal Server Error

---

## 🎨 Design Patterns

### 1. Singleton Pattern

**Purpose:** Ensure a single shared instance across the application

**Implementations:**

#### A. LogisticsConfig
Manages application-wide configuration settings.

```java
LogisticsConfig config = LogisticsConfig.getInstance();
String maxWeight = config.getConfig("MAX_SHIPMENT_WEIGHT");
```

**Why Singleton?**
- Single source of truth for configuration
- Prevents multiple configuration instances
- Thread-safe access to shared settings

#### B. LoggingService
Centralized logging across all components.

```java
LoggingService logger = LoggingService.getInstance();
logger.info("Shipment created successfully");
logger.error("Database connection failed");
```

**Benefits:**
- Consistent logging format
- Single log history
- Easy to redirect output (console, file, etc.)

---

### 2. Factory Pattern

**Purpose:** Create objects without exposing creation logic

**Implementations:**

#### A. ShipmentFactory
Creates different shipment types based on string input.

```java
@Component
public class ShipmentFactory {
    public Shipment createShipment(String type) {
        switch (type.toUpperCase()) {
            case "EXPRESS": return new ExpressShipment();
            case "STANDARD": return new StandardShipment();
            case "ECONOMY": return new EconomyShipment();
            default: throw new IllegalArgumentException("Unknown type");
        }
    }
}
```

**Usage in Repository:**
```java
Shipment shipment = shipmentFactory.createShipment("EXPRESS");
```

**Advantages:**
- **Open-Closed Principle**: Easy to add new shipment types
- Client code doesn't need to know concrete classes
- Centralized object creation logic

#### B. VehicleFactory
Creates Air, Sea, or Land vehicles.

```java
Vehicle vehicle = vehicleFactory.createVehicle("AIR");
```

---

### 3. Builder Pattern

**Purpose:** Construct complex objects step-by-step

**Implementation: ShipmentBuilder**

```java
Shipment shipment = new ShipmentBuilder()
    .expressShipment()
    .trackingNumber("SHP-2024-100")
    .sender("Company A")
    .recipient("Customer B")
    .from("Astana")
    .to("Almaty")
    .weight(150.0)
    .fragile(true)
    .vehicleId(1)
    .build();
```

**Benefits:**
- Fluent, readable API
- Optional parameters handling
- Validation before object creation
- Immutability support

**Used in Service Layer:**
```java
ShipmentBuilder builder = new ShipmentBuilder()
    .type(dto.getShipmentType())
    .trackingNumber(dto.getTrackingNumber())
    .sender(dto.getSenderName())
    .recipient(dto.getRecipientName())
    .weight(dto.getWeight());

Shipment shipment = builder.build();
```

---

## 📦 Component Principles

### REP - Reuse/Release Equivalence Principle

**"Classes that are reused together should be grouped together"**

**Our Implementation:**

```
patterns/
  ├── singleton/
  │   ├── LogisticsConfig.java
  │   └── LoggingService.java
  ├── factory/
  │   ├── ShipmentFactory.java
  │   └── VehicleFactory.java
  └── builder/
      └── ShipmentBuilder.java
```

All design patterns are grouped in the `patterns` package, making them:
- Easy to reuse in other projects
- Releasable as a separate module
- Clear in their purpose

---

### CCP - Common Closure Principle

**"Classes that change together should be grouped together"**

**Our Implementation:**

```
model/
  ├── Shipment.java (abstract)
  ├── ExpressShipment.java
  ├── StandardShipment.java
  └── EconomyShipment.java
```

All shipment types are in the `model` package because:
- Changes to shipment logic affect all types
- New shipment types are added here
- Reduces cascading changes across packages

Similarly:
```
exception/
  ├── InvalidInputException.java
  ├── DuplicateResourceException.java
  ├── ResourceNotFoundException.java
  └── DatabaseOperationException.java
```

---

### CRP - Common Reuse Principle

**"Don't force users to depend on things they don't use"**

**Our Implementation:**

Separate DTOs from Models:
```
dto/
  ├── ShipmentDTO.java
  ├── VehicleDTO.java
  ├── WarehouseDTO.java
  └── ErrorResponse.java
```

- Controllers only need DTOs
- Services convert between Models and DTOs
- Repository only works with Models
- No unnecessary dependencies

---

## 🏛️ SOLID & OOP

### S - Single Responsibility Principle

Each class has ONE reason to change:

- **Controller**: Handle HTTP requests/responses only
- **Service**: Business logic and validation
- **Repository**: Database operations
- **DTO**: Data transfer between layers
- **Model**: Domain logic and behavior

**Example:**
```java
@RestController
public class ShipmentController {
    // ONLY handles HTTP - no business logic
    @PostMapping
    public ResponseEntity<ShipmentDTO> createShipment(@RequestBody ShipmentDTO dto) {
        return ResponseEntity.ok(shipmentService.createShipment(dto));
    }
}
```

---

### O - Open-Closed Principle

Open for extension, closed for modification.

**Example:** Adding a new shipment type
```java
// No need to modify existing code
public class BulkShipment extends Shipment {
    @Override
    public Double calculateShippingCost() {
        return getWeight() * 3.0; // Bulk rate
    }
}

// Factory automatically supports it
shipmentFactory.createShipment("BULK"); // Just add to switch case
```

---

### L - Liskov Substitution Principle

Subclasses can replace parent class without breaking functionality.

**Example:**
```java
Shipment shipment = new ExpressShipment(); // Or StandardShipment, EconomyShipment
Double cost = shipment.calculateShippingCost(); // Works for all
Integer days = shipment.getEstimatedDeliveryDays(); // Works for all
```

All shipment types can be used through the `Shipment` interface.

---

### I - Interface Segregation Principle

Clients shouldn't depend on interfaces they don't use.

**Our approach:**
- Separate DTOs for each entity (ShipmentDTO, VehicleDTO)
- No "God" interface with all methods
- Services expose only necessary methods

---

### D - Dependency Inversion Principle

Depend on abstractions, not concrete classes.

**Example:**
```java
@Service
public class ShipmentService {
    private final ShipmentRepository repository; // Depends on abstraction
    
    @Autowired
    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository; // Injected by Spring
    }
}
```

---

### Advanced OOP Features

#### 1. Abstraction
```java
public abstract class Shipment extends BaseEntity {
    public abstract Double calculateShippingCost();
    public abstract Integer getEstimatedDeliveryDays();
}
```

#### 2. Inheritance
```java
public class ExpressShipment extends Shipment {
    @Override
    public Double calculateShippingCost() {
        return getWeight() * 15.0;
    }
}
```

#### 3. Polymorphism
```java
List<Shipment> shipments = Arrays.asList(
    new ExpressShipment(),
    new StandardShipment(),
    new EconomyShipment()
);

for (Shipment s : shipments) {
    System.out.println(s.calculateShippingCost()); // Different for each
}
```

#### 4. Encapsulation
```java
private String trackingNumber;

public void setTrackingNumber(String trackingNumber) {
    if (trackingNumber == null || trackingNumber.isEmpty()) {
        throw new IllegalArgumentException("Tracking number required");
    }
    this.trackingNumber = trackingNumber;
}
```

#### 5. Composition
```java
public class Shipment {
    private Integer warehouseId; // HAS-A relationship
    private Integer vehicleId;
}
```

---

## 🗄️ Database Schema

### Tables

#### shipments
```sql
CREATE TABLE shipments (
    id SERIAL PRIMARY KEY,
    tracking_number VARCHAR(100) UNIQUE NOT NULL,
    shipment_type VARCHAR(50) NOT NULL,
    sender_name VARCHAR(255) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    origin VARCHAR(500) NOT NULL,
    destination VARCHAR(500) NOT NULL,
    weight DECIMAL(10,2) NOT NULL CHECK (weight > 0),
    status VARCHAR(50) DEFAULT 'PENDING',
    priority VARCHAR(20) DEFAULT 'NORMAL',
    estimated_delivery DATE,
    vehicle_id INTEGER REFERENCES vehicles(id),
    warehouse_id INTEGER REFERENCES warehouses(id),
    is_fragile BOOLEAN DEFAULT FALSE,
    temperature_controlled BOOLEAN DEFAULT FALSE,
    customs_cleared BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### vehicles
```sql
CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    vehicle_type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    license_plate VARCHAR(50) UNIQUE NOT NULL,
    capacity DECIMAL(10,2) NOT NULL CHECK (capacity > 0),
    status VARCHAR(50) DEFAULT 'AVAILABLE',
    max_altitude INTEGER,
    cargo_type VARCHAR(100),
    fuel_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### warehouses
```sql
CREATE TABLE warehouses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(500) NOT NULL,
    capacity INTEGER NOT NULL CHECK (capacity > 0),
    current_load INTEGER DEFAULT 0 CHECK (current_load >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Relationships

- `shipments.vehicle_id` → `vehicles.id` (Many-to-One)
- `shipments.warehouse_id` → `warehouses.id` (Many-to-One)

### Constraints

- **Primary Keys**: All tables have auto-increment `id`
- **Unique**: `tracking_number`, `license_plate`
- **Foreign Keys**: Cascade rules for referential integrity
- **Check Constraints**: Weight > 0, Capacity > 0
- **Indexes**: On `tracking_number`, `status` for performance

---

## 🏗️ System Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│         REST Controllers             │  ← HTTP Requests/Responses
│  (ShipmentController, etc.)          │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         Service Layer                │  ← Business Logic
│  (ShipmentService, etc.)             │     Validation
│                                       │     DTO ↔ Model conversion
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         Repository Layer             │  ← Database Operations
│  (ShipmentRepository, etc.)          │     JDBC
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         Database (PostgreSQL)        │
└─────────────────────────────────────┘
```

### Package Structure

```
src/main/java/com/logistics/
├── controller/          # REST endpoints
│   ├── ShipmentController.java
│   ├── VehicleController.java
│   └── WarehouseController.java
├── service/             # Business logic
│   ├── ShipmentService.java
│   ├── VehicleService.java
│   └── WarehouseService.java
├── repository/          # Database access
│   ├── ShipmentRepository.java
│   ├── VehicleRepository.java
│   └── WarehouseRepository.java
├── model/               # Domain entities
│   ├── BaseEntity.java
│   ├── Shipment.java
│   ├── ExpressShipment.java
│   ├── StandardShipment.java
│   ├── EconomyShipment.java
│   ├── Vehicle.java
│   ├── AirVehicle.java
│   ├── SeaVehicle.java
│   ├── LandVehicle.java
│   └── Warehouse.java
├── dto/                 # Data Transfer Objects
│   ├── ShipmentDTO.java
│   ├── VehicleDTO.java
│   ├── WarehouseDTO.java
│   └── ErrorResponse.java
├── exception/           # Custom exceptions
│   ├── InvalidInputException.java
│   ├── DuplicateResourceException.java
│   ├── ResourceNotFoundException.java
│   ├── DatabaseOperationException.java
│   └── GlobalExceptionHandler.java
├── patterns/            # Design patterns
│   ├── singleton/
│   │   ├── LogisticsConfig.java
│   │   └── LoggingService.java
│   ├── factory/
│   │   ├── ShipmentFactory.java
│   │   └── VehicleFactory.java
│   └── builder/
│       └── ShipmentBuilder.java
└── LogisticsApplication.java
```

---

## 🚀 Installation & Setup

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **PostgreSQL 12+** or **MySQL 8+**
- **Postman** or **curl** (for testing)

### Step 1: Clone Repository

```bash
git clone https://github.com/yourusername/logistics-api.git
cd logistics-api
```

### Step 2: Database Setup

#### For PostgreSQL:

```bash
# Create database
createdb logistics_db

# Run schema
psql logistics_db < src/main/resources/schema.sql
```

#### For MySQL:

```bash
# Create database
mysql -u root -p
CREATE DATABASE logistics_db;
exit;

# Run schema
mysql -u root -p logistics_db < src/main/resources/schema.sql
```

### Step 3: Configure Database

Edit `src/main/resources/application.properties`:

```properties
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/logistics_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# Or MySQL
# spring.datasource.url=jdbc:mysql://localhost:3306/logistics_db
# spring.datasource.username=root
# spring.datasource.password=your_password
```

### Step 4: Build & Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Or using the JAR:

```bash
mvn package
java -jar target/logistics-api-1.0.0.jar
```

### Step 5: Verify

```bash
curl http://localhost:8080/api/shipments
```

You should see a JSON response with existing shipments.

---

## 🧪 API Testing

### Using Postman

1. **Import Collection**: Import the Postman collection (if provided)
2. **Set Base URL**: `http://localhost:8080`

### Example Requests

#### Create Express Shipment

```bash
curl -X POST http://localhost:8080/api/shipments \
  -H "Content-Type: application/json" \
  -d '{
    "shipmentType": "EXPRESS",
    "trackingNumber": "SHP-2024-100",
    "senderName": "Tech Corp",
    "recipientName": "Retail Store",
    "origin": "Astana",
    "destination": "Almaty",
    "weight": 150.0,
    "isFragile": true
  }'
```

#### Get All Shipments

```bash
curl -X GET http://localhost:8080/api/shipments
```

#### Update Shipment Status

```bash
curl -X PUT http://localhost:8080/api/shipments/1 \
  -H "Content-Type: application/json" \
  -d '{
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
  }'
```

#### Delete Shipment

```bash
curl -X DELETE http://localhost:8080/api/shipments/1
```

---

## 💭 Reflection

### What I Learned

1. **Design Patterns in Practice**
   - Understanding when and why to use Singleton, Factory, and Builder
   - How patterns solve real architectural problems
   - The trade-offs between flexibility and complexity

2. **SOLID Principles**
   - How SRP makes code easier to test and maintain
   - OCP allows adding features without modifying existing code
   - DIP through Spring's dependency injection

3. **Spring Boot Ecosystem**
   - Rapid REST API development
   - Automatic dependency injection
   - Global exception handling with @ControllerAdvice

4. **Component Organization**
   - Package structure impacts maintainability
   - Grouping by feature vs. layer
   - The importance of clear boundaries

### Challenges Faced

1. **Database Connection Management**
   - Handling connection pooling with DataSource
   - Proper resource cleanup in JDBC
   - Solution: Try-with-resources and Spring's automatic management

2. **DTO ↔ Model Conversion**
   - Mapping between layers while preserving type information
   - Handling polymorphic types in DTOs
   - Solution: Type-specific handling in conversion methods

3. **Exception Handling**
   - Consistent error responses across all endpoints
   - Mapping SQL exceptions to business exceptions
   - Solution: Global exception handler with standardized ErrorResponse

4. **Builder Pattern Complexity**
   - Validating required vs. optional fields
   - Integrating with Factory pattern
   - Solution: Validation in build() method, delegating creation to factory

### Benefits of This Architecture

✅ **Scalability**: Easy to add new entities or shipment types  
✅ **Testability**: Clear separation of concerns  
✅ **Maintainability**: Single responsibility per class  
✅ **Flexibility**: Open-Closed principle allows extensions  
✅ **Professional**: Follows industry-standard practices  

### Future Improvements

- [ ] Add authentication/authorization (Spring Security)
- [ ] Implement pagination for large datasets
- [ ] Add caching layer (Redis)
- [ ] Implement event-driven architecture
- [ ] Add comprehensive unit and integration tests
- [ ] Create Swagger/OpenAPI documentation
- [ ] Implement GraphQL alternative
- [ ] Add real-time tracking with WebSockets

---

## 📄 License

This project is for educational purposes as part of the Endterm Project.

---

## 👤 Author

**Kurbanbai Sayan** 
University: Astana It University
Course: Advanced Object-Oriented Programming  
Year: 2024

---

## 📚 References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Design Patterns: Elements of Reusable Object-Oriented Software](https://en.wikipedia.org/wiki/Design_Patterns)
- [Clean Code by Robert C. Martin](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
- [RESTful API Design Best Practices](https://restfulapi.net/)

---


