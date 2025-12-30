package com.example.mandates.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StringUtils.
 */
class StringUtilsTest {
    
    @Test
    void testNormalizeRegistration_ValidInput() {
        assertEquals("2023/123456/07", StringUtils.normalizeRegistration("2023/123456/07"));
        assertEquals("2023/123456/07", StringUtils.normalizeRegistration("2023 / 123456 / 07"));
        assertEquals("2023/123456/07", StringUtils.normalizeRegistration("2023  /  123456  /  07"));
        assertEquals("2023/123456/07", StringUtils.normalizeRegistration("2023/123456/07"));
    }
    
    @Test
    void testNormalizeRegistration_LowerCaseInput() {
        assertEquals("ABC/123456/07", StringUtils.normalizeRegistration("abc/123456/07"));
    }
    
    @Test
    void testNormalizeRegistration_NullInput() {
        assertNull(StringUtils.normalizeRegistration(null));
    }
    
    @Test
    void testDeduplicateCommaString_WithDuplicates() {
        assertEquals("apple,banana,cherry", 
                StringUtils.deduplicateCommaString("apple,banana,apple,cherry"));
        assertEquals("one,two,three", 
                StringUtils.deduplicateCommaString("one,two,three,one,two"));
    }
    
    @Test
    void testDeduplicateCommaString_NoDuplicates() {
        assertEquals("apple,banana,cherry", 
                StringUtils.deduplicateCommaString("apple,banana,cherry"));
    }
    
    @Test
    void testDeduplicateCommaString_WithSpaces() {
        assertEquals("apple,banana,cherry", 
                StringUtils.deduplicateCommaString("apple, banana, cherry"));
        assertEquals("apple,banana,cherry", 
                StringUtils.deduplicateCommaString("  apple  ,  banana  ,  cherry  "));
    }
    
    @Test
    void testDeduplicateCommaString_EmptyAndNull() {
        assertNull(StringUtils.deduplicateCommaString(null));
        assertEquals("", StringUtils.deduplicateCommaString(""));
        assertEquals("   ", StringUtils.deduplicateCommaString("   "));
    }
    
    @Test
    void testSanitize_ValidInput() {
        assertEquals("hello world", StringUtils.sanitize("hello world"));
        assertEquals("hello123", StringUtils.sanitize("hello123"));
    }
    
    @Test
    void testSanitize_WithSpecialCharacters() {
        assertEquals("helloworld", StringUtils.sanitize("hello@#$%world"));
        assertEquals("hello  world", StringUtils.sanitize("hello @#$% world")); // Preserves spaces
        assertEquals("test123", StringUtils.sanitize("test!@#123$%^"));
        assertEquals("email", StringUtils.sanitize("e-m-a-i-l"));
    }
    
    @Test
    void testSanitize_NullInput() {
        assertNull(StringUtils.sanitize(null));
    }
    
    @Test
    void testCapitalizeWords_ValidInput() {
        assertEquals("Hello World", StringUtils.capitalizeWords("hello world"));
        assertEquals("Hello World", StringUtils.capitalizeWords("HELLO WORLD"));
        assertEquals("Hello World", StringUtils.capitalizeWords("hELLo WoRLd"));
    }
    
    @Test
    void testCapitalizeWords_SingleWord() {
        assertEquals("Hello", StringUtils.capitalizeWords("hello"));
        assertEquals("Hello", StringUtils.capitalizeWords("HELLO"));
    }
    
    @Test
    void testCapitalizeWords_MultipleSpaces() {
        assertEquals("Hello World", StringUtils.capitalizeWords("hello   world"));
        assertEquals("Hello World Test", StringUtils.capitalizeWords("  hello   world   test  "));
    }
    
    @Test
    void testCapitalizeWords_EmptyAndNull() {
        assertNull(StringUtils.capitalizeWords(null));
        assertEquals("", StringUtils.capitalizeWords(""));
        assertEquals("   ", StringUtils.capitalizeWords("   ")); // Returns original input when trimmed is empty
    }
    
    @Test
    void testCapitalizeWords_SingleCharacter() {
        assertEquals("H", StringUtils.capitalizeWords("h"));
        assertEquals("H W", StringUtils.capitalizeWords("h w"));
    }
}
