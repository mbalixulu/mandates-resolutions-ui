package com.example.mandates.util;

import java.util.regex.Pattern;

/**
 * Utility class for validation operations.
 * Provides reusable validation methods for common data formats.
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern REGISTRATION_PATTERN = 
        Pattern.compile("^[0-9]{4}/[0-9]{6}/[0-9]{2}$");
    
    private static final Pattern ID_NUMBER_PATTERN = 
        Pattern.compile("^[0-9]{13}$");
    
    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Validates an email address format.
     * 
     * @param email the email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates a company registration number format.
     * Expected format: YYYY/NNNNNN/NN (e.g., 2023/123456/07)
     * 
     * @param registration the registration number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRegistration(String registration) {
        if (registration == null || registration.trim().isEmpty()) {
            return false;
        }
        return REGISTRATION_PATTERN.matcher(registration.trim()).matches();
    }
    
    /**
     * Validates a South African ID number format.
     * Expected format: 13 digits
     * 
     * @param idNumber the ID number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIdNumber(String idNumber) {
        if (idNumber == null || idNumber.trim().isEmpty()) {
            return false;
        }
        return ID_NUMBER_PATTERN.matcher(idNumber.trim()).matches();
    }
    
    /**
     * Validates if a string is not null and not empty after trimming.
     * 
     * @param value the string to validate
     * @return true if not blank, false otherwise
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
