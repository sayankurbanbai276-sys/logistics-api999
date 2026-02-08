package com.logistics.patterns.singleton;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * SINGLETON PATTERN - Application Configuration Manager
 *
 * Purpose: Ensures single instance of configuration across the application
 * Thread-safe implementation using Spring's @Component
 */
@Component
public class LogisticsConfig {

    private static LogisticsConfig instance;
    private Map<String, String> configurations;

    // Private constructor to prevent direct instantiation
    private LogisticsConfig() {
        configurations = new HashMap<>();
        loadDefaultConfigurations();
    }

    // Thread-safe singleton getInstance method
    public static synchronized LogisticsConfig getInstance() {
        if (instance == null) {
            instance = new LogisticsConfig();
        }
        return instance;
    }

    private void loadDefaultConfigurations() {
        configurations.put("MAX_SHIPMENT_WEIGHT", "50000.0");
        configurations.put("DEFAULT_CURRENCY", "USD");
        configurations.put("WAREHOUSE_DEFAULT_CAPACITY", "10000");
        configurations.put("EXPRESS_DELIVERY_DAYS", "2");
        configurations.put("STANDARD_DELIVERY_DAYS", "5");
        configurations.put("ECONOMY_DELIVERY_DAYS", "10");
        configurations.put("MAX_RETRY_ATTEMPTS", "3");
    }

    public String getConfig(String key) {
        return configurations.getOrDefault(key, "");
    }

    public void setConfig(String key, String value) {
        configurations.put(key, value);
    }

    public Map<String, String> getAllConfigurations() {
        return new HashMap<>(configurations);
    }

    public Integer getIntConfig(String key, Integer defaultValue) {
        try {
            return Integer.parseInt(getConfig(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Double getDoubleConfig(String key, Double defaultValue) {
        try {
            return Double.parseDouble(getConfig(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
