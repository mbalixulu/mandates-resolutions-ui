package com.example.mandates.service;

import com.example.mandates.dto.DirectorValidationDTO;
import com.example.mandates.dto.MandateRequestDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.util.StringUtils;
import com.example.mandates.util.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MandatesResolutionServiceTest {
    
    @Mock
    private ValidationUtils validationUtils;
    
    @Mock
    private StringUtils stringUtils;
    
    @InjectMocks
    private MandatesResolutionService service;
    
    @Test
    void testProcessRequest_ValidRequest() {
        // Arrange
        MandateRequestDTO request = MandateRequestDTO.builder()
                .mandateNumber("MAN-001")
                .companyRegistration("2023/123456/07")
                .companyName("Test Company")
                .build();
        
        when(stringUtils.normalizeRegistration(anyString())).thenAnswer(i -> i.getArgument(0));
        when(stringUtils.capitalizeWords(anyString())).thenAnswer(i -> i.getArgument(0));
        when(validationUtils.isValidRegistration(anyString())).thenReturn(true);
        
        // Act
        MandateResponseDTO response = service.processRequest(request);
        
        // Assert
        assertNotNull(response);
        assertEquals("MAN-001", response.getMandateNumber());
        assertEquals("PENDING", response.getStatus());
        assertNotNull(response.getId());
    }
    
    @Test
    void testProcessRequest_InvalidRegistration() {
        // Arrange
        MandateRequestDTO request = MandateRequestDTO.builder()
                .mandateNumber("MAN-001")
                .companyRegistration("invalid")
                .companyName("Test Company")
                .build();
        
        when(stringUtils.normalizeRegistration(anyString())).thenAnswer(i -> i.getArgument(0));
        when(validationUtils.isValidRegistration(anyString())).thenReturn(false);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.processRequest(request));
    }
    
    @Test
    void testHandleDirectorValidation_Valid() {
        // Arrange
        DirectorValidationDTO validation = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("9001015009087")
                .email("john@example.com")
                .build();
        
        when(stringUtils.capitalizeWords(anyString())).thenAnswer(i -> i.getArgument(0));
        when(validationUtils.isValidIdNumber(anyString())).thenReturn(true);
        when(validationUtils.isValidEmail(anyString())).thenReturn(true);
        
        // Act
        boolean result = service.handleDirectorValidation(validation);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    void testHandleDirectorValidation_InvalidIdNumber() {
        // Arrange
        DirectorValidationDTO validation = DirectorValidationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .idNumber("invalid")
                .email("john@example.com")
                .build();
        
        when(validationUtils.isValidIdNumber(anyString())).thenReturn(false);
        
        // Act
        boolean result = service.handleDirectorValidation(validation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void testGetMandateById_Exists() {
        // Arrange
        MandateRequestDTO request = MandateRequestDTO.builder()
                .mandateNumber("MAN-001")
                .companyRegistration("2023/123456/07")
                .companyName("Test Company")
                .build();
        
        when(stringUtils.normalizeRegistration(anyString())).thenAnswer(i -> i.getArgument(0));
        when(stringUtils.capitalizeWords(anyString())).thenAnswer(i -> i.getArgument(0));
        when(validationUtils.isValidRegistration(anyString())).thenReturn(true);
        MandateResponseDTO created = service.processRequest(request);
        
        // Act
        Optional<MandateResponseDTO> found = service.getMandateById(created.getId());
        
        // Assert
        assertTrue(found.isPresent());
        assertEquals("MAN-001", found.get().getMandateNumber());
    }
    
    @Test
    void testGetMandateById_NotExists() {
        // Act
        Optional<MandateResponseDTO> found = service.getMandateById(999L);
        
        // Assert
        assertFalse(found.isPresent());
    }
    
    @Test
    void testUpdateMandateStatus_Success() {
        // Arrange
        MandateRequestDTO request = MandateRequestDTO.builder()
                .mandateNumber("MAN-001")
                .companyRegistration("2023/123456/07")
                .companyName("Test Company")
                .build();
        
        when(stringUtils.normalizeRegistration(anyString())).thenAnswer(i -> i.getArgument(0));
        when(stringUtils.capitalizeWords(anyString())).thenAnswer(i -> i.getArgument(0));
        when(validationUtils.isValidRegistration(anyString())).thenReturn(true);
        MandateResponseDTO created = service.processRequest(request);
        
        // Act
        MandateResponseDTO updated = service.updateMandateStatus(created.getId(), "APPROVED");
        
        // Assert
        assertEquals("APPROVED", updated.getStatus());
    }
    
    @Test
    void testUpdateMandateStatus_NotFound() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                () -> service.updateMandateStatus(999L, "APPROVED"));
    }
}
