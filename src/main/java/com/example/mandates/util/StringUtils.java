package com.example.mandates.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for string manipulation operations.
 * Contains reusable string processing logic extracted from the original controller.
 */
@Component
public class StringUtils {
    
    /**
     * Normalizes a registration number by removing spaces and standardizing format.
     * Extracted from original controller method normReg.
     * 
     * @param registration the raw registration number
     * @return normalized registration number
     */
    public String normalizeRegistration(String registration) {
        if (registration == null || registration.isEmpty()) {
            return "";
        }
        
        return registration.replaceAll("\\s+", "").trim().toUpperCase();
    }
    
    /**
     * Removes duplicate values from a comma-separated string.
     * Extracted from original controller method dedupeComma.
     * 
     * @param input comma-separated string
     * @return deduplicated comma-separated string
     */
    public String deduplicateCommaString(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.joining(","));
    }
    
    /**
     * Sanitizes input by removing special characters.
     * 
     * @param input the input string
     * @return sanitized string
     */
    public String sanitize(String input) {
        if (input == null) {
            return "";
        }
        
        return input.replaceAll("[^a-zA-Z0-9\\s,.-]", "").trim();
    }
    
    /**
     * Capitalizes the first letter of each word.
     * 
     * @param input the input string
     * @return capitalized string
     */
    public String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        
        return Arrays.stream(input.split("\\s+"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
