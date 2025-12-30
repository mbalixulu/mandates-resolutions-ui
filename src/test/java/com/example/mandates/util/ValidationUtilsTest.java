package com.example.mandates.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationUtils.
 */
class ValidationUtilsTest {
    
    @Test
    void testIsValidEmail_ValidEmail() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"));
        assertTrue(ValidationUtils.isValidEmail("user.name@domain.co.za"));
        assertTrue(ValidationUtils.isValidEmail("first.last@company.org"));
    }
    
    @Test
    void testIsValidEmail_InvalidEmail() {
        assertFalse(ValidationUtils.isValidEmail("invalid"));
        assertFalse(ValidationUtils.isValidEmail("@example.com"));
        assertFalse(ValidationUtils.isValidEmail("user@"));
        assertFalse(ValidationUtils.isValidEmail("user@domain"));
        assertFalse(ValidationUtils.isValidEmail(null));
        assertFalse(ValidationUtils.isValidEmail(""));
        assertFalse(ValidationUtils.isValidEmail("   "));
    }
    
    @Test
    void testIsValidRegistration_ValidRegistration() {
        assertTrue(ValidationUtils.isValidRegistration("2023/123456/07"));
        assertTrue(ValidationUtils.isValidRegistration("2020/000001/01"));
        assertTrue(ValidationUtils.isValidRegistration("1999/999999/99"));
    }
    
    @Test
    void testIsValidRegistration_InvalidRegistration() {
        assertFalse(ValidationUtils.isValidRegistration("2023-123456-07"));
        assertFalse(ValidationUtils.isValidRegistration("2023/12345/07"));
        assertFalse(ValidationUtils.isValidRegistration("2023/1234567/07"));
        assertFalse(ValidationUtils.isValidRegistration("23/123456/07"));
        assertFalse(ValidationUtils.isValidRegistration("2023/123456/7"));
        assertFalse(ValidationUtils.isValidRegistration(null));
        assertFalse(ValidationUtils.isValidRegistration(""));
        assertFalse(ValidationUtils.isValidRegistration("   "));
    }
    
    @Test
    void testIsValidIdNumber_ValidIdNumber() {
        assertTrue(ValidationUtils.isValidIdNumber("9001015009087"));
        assertTrue(ValidationUtils.isValidIdNumber("8512305123456"));
        assertTrue(ValidationUtils.isValidIdNumber("0123456789012"));
    }
    
    @Test
    void testIsValidIdNumber_InvalidIdNumber() {
        assertFalse(ValidationUtils.isValidIdNumber("901015009087")); // 12 digits
        assertFalse(ValidationUtils.isValidIdNumber("90010150090871")); // 14 digits
        assertFalse(ValidationUtils.isValidIdNumber("900101500908A"));
        assertFalse(ValidationUtils.isValidIdNumber(null));
        assertFalse(ValidationUtils.isValidIdNumber(""));
        assertFalse(ValidationUtils.isValidIdNumber("   "));
    }
    
    @Test
    void testIsNotBlank_ValidStrings() {
        assertTrue(ValidationUtils.isNotBlank("hello"));
        assertTrue(ValidationUtils.isNotBlank("  world  "));
        assertTrue(ValidationUtils.isNotBlank("test"));
    }
    
    @Test
    void testIsNotBlank_InvalidStrings() {
        assertFalse(ValidationUtils.isNotBlank(null));
        assertFalse(ValidationUtils.isNotBlank(""));
        assertFalse(ValidationUtils.isNotBlank("   "));
        assertFalse(ValidationUtils.isNotBlank("\t\n"));
    }
}
