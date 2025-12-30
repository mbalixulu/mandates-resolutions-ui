package com.example.mandates.service;

import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.model.Mandate;
import com.example.mandates.util.StringUtils;
import com.example.mandates.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Mandate Search operations.
 * Handles business logic related to searching and filtering mandates.
 * This service is responsible for:
 * - Searching mandates by company registration
 * - Filtering mandates by status
 * - Advanced mandate queries
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MandateSearchService {
    
    private final AdminApprovalService adminApprovalService;
    
    /**
     * Searches mandates by company registration number.
     * 
     * @param registration the company registration number
     * @return list of matching mandates
     * @throws IllegalArgumentException if registration format is invalid
     */
    public List<MandateResponseDTO> searchByRegistration(String registration) {
        log.info("Searching mandates by registration: {}", registration);
        
        // Normalize and validate registration
        String normalizedReg = StringUtils.normalizeRegistration(registration);
        if (!ValidationUtils.isValidRegistration(normalizedReg)) {
            throw new IllegalArgumentException("Invalid company registration format");
        }
        
        return adminApprovalService.getMandateStore().values().stream()
                .filter(mandate -> mandate.getCompanyRegistration().equals(normalizedReg))
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    /**
     * Searches mandates by status.
     * 
     * @param status the mandate status (DRAFT, PENDING_APPROVAL, APPROVED, REJECTED)
     * @return list of mandates with the specified status
     */
    public List<MandateResponseDTO> searchByStatus(String status) {
        log.info("Searching mandates by status: {}", status);
        
        if (!ValidationUtils.isNotBlank(status)) {
            throw new IllegalArgumentException("Status cannot be blank");
        }
        
        String normalizedStatus = status.toUpperCase();
        
        return adminApprovalService.getMandateStore().values().stream()
                .filter(mandate -> mandate.getStatus().equals(normalizedStatus))
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    /**
     * Retrieves all mandates.
     * 
     * @return list of all mandates
     */
    public List<MandateResponseDTO> getAllMandates() {
        log.info("Retrieving all mandates");
        return adminApprovalService.getMandateStore().values().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    /**
     * Searches mandates by creator.
     * 
     * @param createdBy the username of the creator
     * @return list of mandates created by the user
     */
    public List<MandateResponseDTO> searchByCreator(String createdBy) {
        log.info("Searching mandates by creator: {}", createdBy);
        
        if (!ValidationUtils.isNotBlank(createdBy)) {
            throw new IllegalArgumentException("Creator name cannot be blank");
        }
        
        return adminApprovalService.getMandateStore().values().stream()
                .filter(mandate -> mandate.getCreatedBy().equalsIgnoreCase(createdBy))
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    /**
     * Searches mandates by multiple criteria.
     * 
     * @param registration optional company registration
     * @param status optional status
     * @param createdBy optional creator name
     * @return list of mandates matching all provided criteria
     */
    public List<MandateResponseDTO> advancedSearch(String registration, String status, String createdBy) {
        log.info("Advanced search - registration: {}, status: {}, createdBy: {}", 
                registration, status, createdBy);
        
        return adminApprovalService.getMandateStore().values().stream()
                .filter(mandate -> {
                    if (ValidationUtils.isNotBlank(registration)) {
                        String normalizedReg = StringUtils.normalizeRegistration(registration);
                        if (!mandate.getCompanyRegistration().equals(normalizedReg)) {
                            return false;
                        }
                    }
                    if (ValidationUtils.isNotBlank(status)) {
                        if (!mandate.getStatus().equalsIgnoreCase(status)) {
                            return false;
                        }
                    }
                    if (ValidationUtils.isNotBlank(createdBy)) {
                        if (!mandate.getCreatedBy().equalsIgnoreCase(createdBy)) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    /**
     * Helper method to map Mandate entity to MandateResponseDTO.
     */
    private MandateResponseDTO mapToResponseDTO(Mandate mandate) {
        return MandateResponseDTO.builder()
                .id(mandate.getId())
                .companyRegistration(mandate.getCompanyRegistration())
                .title(mandate.getTitle())
                .description(mandate.getDescription())
                .status(mandate.getStatus())
                .createdBy(mandate.getCreatedBy())
                .createdAt(mandate.getCreatedAt())
                .updatedAt(mandate.getUpdatedAt())
                .approvedBy(mandate.getApprovedBy())
                .approvedAt(mandate.getApprovedAt())
                .build();
    }
}
