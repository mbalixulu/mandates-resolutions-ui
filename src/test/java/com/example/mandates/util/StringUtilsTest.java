package com.example.mandates.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    
    private StringUtils stringUtils;
    
    @BeforeEach
    void setUp() {
        stringUtils = new StringUtils();
    }
    
    @Test
    void testNormalizeRegistration_RemovesSpaces() {
        assertEquals("2023/123456/07", stringUtils.normalizeRegistration("2023 / 123456 / 07"));
    }
    
    @Test
    void testNormalizeRegistration_UpperCase() {
        assertEquals("ABC123", stringUtils.normalizeRegistration("abc123"));
    }
    
    @Test
    void testNormalizeRegistration_Null() {
        assertEquals("", stringUtils.normalizeRegistration(null));
    }
    
    @Test
    void testDeduplicateCommaString_RemovesDuplicates() {
        assertEquals("1,2,3", stringUtils.deduplicateCommaString("1,2,3,2,1"));
    }
    
    @Test
    void testDeduplicateCommaString_TrimsWhitespace() {
        assertEquals("1,2,3", stringUtils.deduplicateCommaString(" 1 , 2 , 3 "));
    }
    
    @Test
    void testDeduplicateCommaString_Null() {
        assertEquals("", stringUtils.deduplicateCommaString(null));
    }
    
    @Test
    void testDeduplicateCommaString_Empty() {
        assertEquals("", stringUtils.deduplicateCommaString(""));
    }
    
    @Test
    void testSanitize_RemovesSpecialCharacters() {
        assertEquals("Hello123", stringUtils.sanitize("Hello@#$123"));
    }
    
    @Test
    void testSanitize_Null() {
        assertEquals("", stringUtils.sanitize(null));
    }
    
    @Test
    void testCapitalizeWords_SingleWord() {
        assertEquals("Hello", stringUtils.capitalizeWords("hello"));
    }
    
    @Test
    void testCapitalizeWords_MultipleWords() {
        assertEquals("Hello World", stringUtils.capitalizeWords("hello world"));
    }
    
    @Test
    void testCapitalizeWords_Null() {
        assertEquals("", stringUtils.capitalizeWords(null));
    }
}
