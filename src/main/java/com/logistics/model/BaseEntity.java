package com.logistics.model;

import java.time.LocalDateTime;

/**
 * Abstract base entity for all domain objects
 * Demonstrates OOP principles: Abstraction, Encapsulation
 * Follows Open-Closed Principle (OCP)
 */
public abstract class BaseEntity {
    private Integer id;
    private String name;
    private LocalDateTime createdAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public BaseEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    // Abstract methods that subclasses must implement
    public abstract String getEntityType();
    public abstract boolean validate();

    // Concrete method shared by all entities
    public String getDisplayInfo() {
        return String.format("%s [ID: %d, Name: %s]",
                getEntityType(), id, name);
    }

    // Getters and Setters (Encapsulation)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}