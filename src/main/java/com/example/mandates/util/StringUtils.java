package com.example.mandates.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class for string manipulation operations.
 * Provides reusable methods for common string transformations.
 */
public class StringUtils {
    
    private StringUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Normalizes a company registration number by removing spaces and converting to uppercase.
     * Example: "2023 / 123456 / 07" becomes "2023/123456/07"
     * 
     * @param registration the registration number to normalize
     * @return normalized registration number
     */
    public static String normalizeRegistration(String registration) {
        if (registration == null) {
            return null;
        }
        return registration.replaceAll("\\s+", "").toUpperCase();
    }
    
    /**
     * Removes duplicate values from a comma-separated string while preserving order.
     * Example: "apple,banana,apple,cherry" becomes "apple,banana,cherry"
     * 
     * @param commaString the comma-separated string
     * @return deduplicated comma-separated string
     */
    public static String deduplicateCommaString(String commaString) {
        if (commaString == null || commaString.trim().isEmpty()) {
            return commaString;
        }
        
        Set<String> uniqueValues = new LinkedHashSet<>(
            Arrays.asList(commaString.split(","))
        );
        
        return uniqueValues.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));
    }
    
    /**
     * Sanitizes a string by removing special characters except spaces.
     * Only allows alphanumeric characters and spaces.
     * 
     * @param input the string to sanitize
     * @return sanitized string
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[^a-zA-Z0-9\\s]", "");
    }
    
    /**
     * Capitalizes the first letter of each word in a string.
     * Example: "hello world" becomes "Hello World"
     * 
     * @param input the string to capitalize
     * @return capitalized string
     */
    public static String capitalizeWords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        
        String[] words = input.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            
            String word = words[i];
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
            }
        }
        
        return result.toString();
    }
}
