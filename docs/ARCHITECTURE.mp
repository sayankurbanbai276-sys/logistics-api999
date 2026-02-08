# System Architecture Documentation

## Component Diagram

```
┌────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                            │
│                  (Postman, Web Browser, Mobile App)             │
└────────────────────────────────┬───────────────────────────────┘
                                 │ HTTP Requests/Responses
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│                    REST CONTROLLERS                             │
│  ┌──────────────────┐ ┌──────────────────┐ ┌────────────────┐ │
│  │ ShipmentController│ │ VehicleController│ │WarehouseCtrl   │ │
│  └──────────────────┘ └──────────────────┘ └────────────────┘ │
└────────────────────────────────┬───────────────────────────────┘
                                 │
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│                    GLOBAL EXCEPTION HANDLER                     │
│                  @ControllerAdvice                              │
└────────────────────────────────────────────────────────────────┘
                                 │
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                              │
│  ┌──────────────────┐ ┌──────────────────┐ ┌────────────────┐ │
│  │ ShipmentService  │ │  VehicleService  │ │WarehouseService│ │
│  │                  │ │                  │ │                │ │
│  │ • Validation     │ │  • Business Logic│ │ • Capacity Mgmt│ │
│  │ • Builder Pattern│ │  • Factory Usage │ │ • Validation   │ │
│  │ • DTO Conversion │ │  • DTO Conv.     │ │ • DTO Conv.    │ │
│  └──────────────────┘ └──────────────────┘ └────────────────┘ │
└────────────────────────────────┬───────────────────────────────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        │                        │                        │
        ▼                        ▼                        ▼
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│ Design Patterns   │  │   Utilities      │  │   Singletons     │
│                   │  │                  │  │                  │
│ • Factory         │  │ • Validators     │  │ • LoggingService │
│ • Builder         │  │ • Converters     │  │ • Config Manager │
└──────────────────┘  └──────────────────┘  └──────────────────┘
                                 │
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│                    REPOSITORY LAYER                             │
│  ┌──────────────────┐ ┌──────────────────┐ ┌────────────────┐ │
│  │ShipmentRepository│ │ VehicleRepository│ │WarehouseRepo   │ │
│  │                  │ │                  │ │                │ │
│  │ • CRUD Ops       │ │  • CRUD Ops      │ │ • CRUD Ops     │ │
│  │ • JDBC           │ │  • JDBC          │ │ • JDBC         │ │
│  │ • Factory Usage  │ │  • Factory Usage │ │ • Direct Obj.  │ │
│  └──────────────────┘ └──────────────────┘ └────────────────┘ │
└────────────────────────────────┬───────────────────────────────┘
                                 │
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│                    DATA SOURCE (Spring)                         │
│                   Connection Pooling                            │
└────────────────────────────────┬───────────────────────────────┘
                                 │
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                               │
│                   PostgreSQL / MySQL                            │
│                                                                  │
│  Tables: shipments, vehicles, warehouses                        │
└────────────────────────────────────────────────────────────────┘
```

## Class Hierarchy

### Shipment Hierarchy
```
BaseEntity (abstract)
    └── Shipment (abstract)
            ├── ExpressShipment
            ├── StandardShipment
            └── EconomyShipment
```

### Vehicle Hierarchy
```
BaseEntity (abstract)
    └── Vehicle (abstract)
            ├── AirVehicle
            ├── SeaVehicle
            └── LandVehicle
```

### Warehouse
```
BaseEntity (abstract)
    └── Warehouse
```

## Request Flow

### Creating a Shipment

1. **Client** sends POST request with ShipmentDTO JSON
2. **ShipmentController** receives request
3. **ShipmentService** is called
   - Validates input (throws InvalidInputException if invalid)
   - Uses ShipmentBuilder to construct Shipment object
   - Builder uses ShipmentFactory internally
4. **ShipmentRepository** persists to database
   - Maps model to SQL
   - Executes PreparedStatement
   - Returns saved Shipment
5. **ShipmentService** converts back to DTO
6. **ShipmentController** returns ResponseEntity with DTO
7. **Client** receives 201 Created with JSON response

### Error Flow

1. **Exception** thrown (e.g., ResourceNotFoundException)
2. **GlobalExceptionHandler** catches it
3. Creates **ErrorResponse** DTO
4. Returns appropriate HTTP status
5. Client receives error JSON

## Design Pattern Integration

### Factory Pattern Flow
```
Controller → Service → Factory.createShipment("EXPRESS")
                           └→ Returns ExpressShipment instance
                       Repository.create(shipment)
                           └→ Polymorphic insert
```

### Builder Pattern Flow
```
Service receives DTO
    └→ new ShipmentBuilder()
          .type("EXPRESS")
          .trackingNumber("SHP-001")
          .sender("ABC")
          .recipient("XYZ")
          .weight(150.0)
          .fragile(true)
          .build()
              └→ Validates
              └→ Uses Factory
              └→ Returns Shipment
```

### Singleton Pattern Flow
```
Any Component
    └→ LoggingService.getInstance()
          └→ Returns same instance
          └→ log("message")
    
    └→ LogisticsConfig.getInstance()
          └→ Returns same instance
          └→ getConfig("key")
```

## Component Principles in Action

### REP - Reuse/Release
- `patterns/` package can be released as library
- `model/` package defines reusable domain
- `dto/` package for API contracts

### CCP - Common Closure
- All Shipment variants in `model/`
- All exceptions in `exception/`
- All DTOs in `dto/`

### CRP - Common Reuse
- Controllers depend on Services, not Repositories
- Services depend on Repositories, not JDBC details
- No circular dependencies

## Thread Safety

### Singleton Instances
- LoggingService: Thread-safe via synchronized getInstance()
- LogisticsConfig: Thread-safe via synchronized getInstance()

### Spring Beans
- Controllers: Request-scoped (new instance per request)
- Services: Singleton by default (stateless)
- Repositories: Singleton by default (stateless)

## Performance Considerations

### Database Connection Pooling
- Spring Boot uses HikariCP by default
- Configurable in application.properties
- Connections reused across requests

### Indexing
- Shipments indexed on tracking_number
- Vehicles indexed on license_plate
- Fast lookups for common queries

### Lazy Loading
- Relationships not eagerly loaded (JDBC)
- On-demand fetching when needed

## Security Considerations (Future)

- Add Spring Security for authentication
- JWT tokens for stateless auth
- Role-based access control (RBAC)
- Input sanitization (already done via validation)
- SQL injection prevention (PreparedStatements)

## Scalability Path

1. **Horizontal Scaling**: Deploy multiple instances behind load balancer
2. **Database Replication**: Read replicas for queries
3. **Caching**: Redis for frequently accessed data
4. **Async Processing**: RabbitMQ for long-running tasks
5. **Microservices**: Split into Shipment, Vehicle, Warehouse services
