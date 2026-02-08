package com.logistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println("Smart Logistics Management System API Started");
        System.out.println("Server running on: http://localhost:8080");
        System.out.println("==============================================\n");
    }
}