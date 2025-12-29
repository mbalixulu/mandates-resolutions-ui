package com.example.mandates.util;

import org.springframework.stereotype.Component;

/**
 * Utility class for validation operations.
 * Contains reusable validation logic extracted from the original controller.
 */
@Component
public class ValidationUtils {
    
    /**
     * Validates if a company registration number is valid.
     * 
     * @param registration the company registration number
     * @return true if valid, false otherwise
     */
    public boolean isValidRegistration(String registration) {
        if (registration == null || registration.trim().isEmpty()) {
            return false;
        }
        
        String normalized = normReg(registration);
        
        // Check if registration matches expected pattern
        return normalized.matches("\\d{4}/\\d{6}/\\d{2}");
    }
    
    /**
     * Validates if an ID number is valid.
     * 
     * @param idNumber the ID number to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidIdNumber(String idNumber) {
        if (idNumber == null || idNumber.trim().isEmpty()) {
            return false;
        }
        
        // South African ID number is 13 digits
        return idNumber.matches("\\d{13}");
    }
    
    /**
     * Validates if an email address is valid.
     * 
     * @param email the email address
     * @return true if valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Normalizes a registration number by removing spaces and formatting.
     * Extracted from original controller.
     * 
     * @param registration the raw registration number
     * @return normalized registration number
     */
    private String normReg(String registration) {
        if (registration == null) {
            return "";
        }
        
        return registration.replaceAll("\\s+", "").trim();
    }
}
