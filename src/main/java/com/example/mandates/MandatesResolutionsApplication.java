package com.example.mandates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Mandates and Resolutions Management System.
 * This application demonstrates a refactored architecture with clear separation
 * of concerns following the Single Responsibility Principle.
 */
@SpringBootApplication
public class MandatesResolutionsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MandatesResolutionsApplication.class, args);
    }
}
