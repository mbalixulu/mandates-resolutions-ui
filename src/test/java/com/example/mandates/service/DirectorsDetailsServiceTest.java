package com.example.mandates.service;

import com.example.mandates.dto.DirectorValidationDTO;
import com.example.mandates.model.Director;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DirectorsDetailsService.
 */
class DirectorsDetailsServiceTest {
    
    private DirectorsDetailsService directorsDetailsService;
    
    @BeforeEach
    void setUp() {
        directorsDetailsService = new DirectorsDetailsService();
    }
    
    @Test
    void testValidateAndStoreDirector_ValidDirector() {
        DirectorValidationDTO dto = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john.doe@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        boolean result = directorsDetailsService.validateAndStoreDirector(dto);
        
        assertTrue(result);
        List<Director> directors = directorsDetailsService.getAllValidatedDirectors();
        assertEquals(1, directors.size());
        assertEquals("John", directors.get(0).getFirstName());
    }
    
    @Test
    void testValidateAndStoreDirector_InvalidIdNumber() {
        DirectorValidationDTO dto = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("12345") // Invalid ID number
                .email("john.doe@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        boolean result = directorsDetailsService.validateAndStoreDirector(dto);
        
        assertFalse(result);
    }
    
    @Test
    void testValidateAndStoreDirector_InvalidEmail() {
        DirectorValidationDTO dto = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("invalid-email") // Invalid email
                .companyRegistration("2023/123456/07")
                .build();
        
        boolean result = directorsDetailsService.validateAndStoreDirector(dto);
        
        assertFalse(result);
    }
    
    @Test
    void testValidateAndStoreDirector_InvalidRegistration() {
        DirectorValidationDTO dto = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john.doe@example.com")
                .companyRegistration("invalid") // Invalid registration
                .build();
        
        boolean result = directorsDetailsService.validateAndStoreDirector(dto);
        
        assertFalse(result);
    }
    
    @Test
    void testValidateAndStoreDirector_DuplicateDirector() {
        DirectorValidationDTO dto = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john.doe@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        // First validation
        boolean result1 = directorsDetailsService.validateAndStoreDirector(dto);
        assertTrue(result1);
        
        // Duplicate validation
        boolean result2 = directorsDetailsService.validateAndStoreDirector(dto);
        assertTrue(result2);
        
        // Should still have only one director
        List<Director> directors = directorsDetailsService.getAllValidatedDirectors();
        assertEquals(1, directors.size());
    }
    
    @Test
    void testGetDirectorById_Found() {
        DirectorValidationDTO dto = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john.doe@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        directorsDetailsService.validateAndStoreDirector(dto);
        
        Optional<Director> result = directorsDetailsService.getDirectorById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
    }
    
    @Test
    void testGetDirectorById_NotFound() {
        Optional<Director> result = directorsDetailsService.getDirectorById(999L);
        assertFalse(result.isPresent());
    }
    
    @Test
    void testGetAllValidatedDirectors() {
        DirectorValidationDTO dto1 = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john.doe@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        DirectorValidationDTO dto2 = DirectorValidationDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .idNumber("8512305123456")
                .email("jane.smith@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        directorsDetailsService.validateAndStoreDirector(dto1);
        directorsDetailsService.validateAndStoreDirector(dto2);
        
        List<Director> directors = directorsDetailsService.getAllValidatedDirectors();
        
        assertEquals(2, directors.size());
    }
    
    @Test
    void testGetDirectorsByCompany() {
        DirectorValidationDTO dto1 = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john.doe@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        DirectorValidationDTO dto2 = DirectorValidationDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .idNumber("8512305123456")
                .email("jane.smith@example.com")
                .companyRegistration("2024/654321/08")
                .build();
        
        directorsDetailsService.validateAndStoreDirector(dto1);
        directorsDetailsService.validateAndStoreDirector(dto2);
        
        List<Director> directors = directorsDetailsService.getDirectorsByCompany("2023/123456/07");
        
        assertEquals(1, directors.size());
        assertEquals("John", directors.get(0).getFirstName());
    }
    
    @Test
    void testIsDirectorValidated_True() {
        DirectorValidationDTO dto = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john.doe@example.com")
                .companyRegistration("2023/123456/07")
                .build();
        
        directorsDetailsService.validateAndStoreDirector(dto);
        
        assertTrue(directorsDetailsService.isDirectorValidated("9001015009087"));
    }
    
    @Test
    void testIsDirectorValidated_False() {
        assertFalse(directorsDetailsService.isDirectorValidated("9999999999999"));
    }
}
