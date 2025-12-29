package com.example.mandates.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {
    
    private ValidationUtils validationUtils;
    
    @BeforeEach
    void setUp() {
        validationUtils = new ValidationUtils();
    }
    
    @Test
    void testIsValidRegistration_ValidFormat() {
        assertTrue(validationUtils.isValidRegistration("2023/123456/07"));
    }
    
    @Test
    void testIsValidRegistration_ValidFormatWithSpaces() {
        assertTrue(validationUtils.isValidRegistration("2023 / 123456 / 07"));
    }
    
    @Test
    void testIsValidRegistration_InvalidFormat() {
        assertFalse(validationUtils.isValidRegistration("2023/12345/07"));
    }
    
    @Test
    void testIsValidRegistration_Null() {
        assertFalse(validationUtils.isValidRegistration(null));
    }
    
    @Test
    void testIsValidRegistration_Empty() {
        assertFalse(validationUtils.isValidRegistration(""));
    }
    
    @Test
    void testIsValidIdNumber_Valid() {
        assertTrue(validationUtils.isValidIdNumber("9001015009087"));
    }
    
    @Test
    void testIsValidIdNumber_TooShort() {
        assertFalse(validationUtils.isValidIdNumber("900101500908"));
    }
    
    @Test
    void testIsValidIdNumber_TooLong() {
        assertFalse(validationUtils.isValidIdNumber("90010150090877"));
    }
    
    @Test
    void testIsValidIdNumber_Null() {
        assertFalse(validationUtils.isValidIdNumber(null));
    }
    
    @Test
    void testIsValidEmail_Valid() {
        assertTrue(validationUtils.isValidEmail("test@example.com"));
    }
    
    @Test
    void testIsValidEmail_Invalid() {
        assertFalse(validationUtils.isValidEmail("invalid-email"));
    }
    
    @Test
    void testIsValidEmail_Null() {
        assertFalse(validationUtils.isValidEmail(null));
    }
}
