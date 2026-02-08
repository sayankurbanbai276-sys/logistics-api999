package com.logistics.patterns.singleton;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * SINGLETON PATTERN - Centralized Logging Service
 *
 * Purpose: Single instance for managing application-wide logging
 */
@Component
public class LoggingService {

    private static LoggingService instance;
    private List<String> logs;
    private DateTimeFormatter formatter;

    private LoggingService() {
        logs = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static synchronized LoggingService getInstance() {
        if (instance == null) {
            instance = new LoggingService();
        }
        return instance;
    }

    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);
        logs.add(logEntry);
        System.out.println(logEntry);
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public List<String> getRecentLogs(int count) {
        int size = logs.size();
        int fromIndex = Math.max(0, size - count);
        return new ArrayList<>(logs.subList(fromIndex, size));
    }

    public void clearLogs() {
        logs.clear();
    }
}
