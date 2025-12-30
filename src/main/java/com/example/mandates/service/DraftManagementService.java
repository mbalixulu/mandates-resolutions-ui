package com.example.mandates.service;

import com.example.mandates.dto.MandateRequestDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.model.Mandate;
import com.example.mandates.util.StringUtils;
import com.example.mandates.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Draft Management operations.
 * Handles business logic related to creating, updating, and managing draft mandates.
 * This service is responsible for:
 * - Creating new draft mandates
 * - Updating draft mandates
 * - Retrieving draft mandates
 * - Submitting drafts for approval
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DraftManagementService {
    
    private final AdminApprovalService adminApprovalService;
    
    /**
     * Creates a new draft mandate.
     * 
     * @param requestDTO the mandate request data
     * @return created mandate response
     * @throws IllegalArgumentException if validation fails
     */
    public MandateResponseDTO createDraft(MandateRequestDTO requestDTO) {
        log.info("Creating new draft mandate for company: {}", requestDTO.getCompanyRegistration());
        
        // Validate and normalize company registration
        String normalizedReg = StringUtils.normalizeRegistration(requestDTO.getCompanyRegistration());
        if (!ValidationUtils.isValidRegistration(normalizedReg)) {
            throw new IllegalArgumentException("Invalid company registration format");
        }
        
        // Create new mandate in DRAFT status
        Mandate mandate = Mandate.builder()
                .id(adminApprovalService.getIdGenerator().getAndIncrement())
                .companyRegistration(normalizedReg)
                .title(StringUtils.capitalizeWords(requestDTO.getTitle()))
                .description(requestDTO.getDescription())
                .status("DRAFT")
                .createdBy(requestDTO.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        adminApprovalService.getMandateStore().put(mandate.getId(), mandate);
        log.info("Draft mandate created with ID: {}", mandate.getId());
        
        return mapToResponseDTO(mandate);
    }
    
    /**
     * Updates an existing draft mandate.
     * 
     * @param id the mandate ID
     * @param requestDTO the updated mandate data
     * @return updated mandate response
     * @throws IllegalArgumentException if mandate not found or not in draft status
     */
    public MandateResponseDTO updateDraft(Long id, MandateRequestDTO requestDTO) {
        log.info("Updating draft mandate ID: {}", id);
        
        Mandate mandate = adminApprovalService.getMandateStore().get(id);
        if (mandate == null) {
            throw new IllegalArgumentException("Mandate not found with ID: " + id);
        }
        
        if (!"DRAFT".equals(mandate.getStatus())) {
            throw new IllegalArgumentException("Can only update mandates in DRAFT status");
        }
        
        // Update mandate fields
        String normalizedReg = StringUtils.normalizeRegistration(requestDTO.getCompanyRegistration());
        if (!ValidationUtils.isValidRegistration(normalizedReg)) {
            throw new IllegalArgumentException("Invalid company registration format");
        }
        
        mandate.setCompanyRegistration(normalizedReg);
        mandate.setTitle(StringUtils.capitalizeWords(requestDTO.getTitle()));
        mandate.setDescription(requestDTO.getDescription());
        mandate.setUpdatedAt(LocalDateTime.now());
        
        adminApprovalService.getMandateStore().put(mandate.getId(), mandate);
        log.info("Draft mandate {} updated", id);
        
        return mapToResponseDTO(mandate);
    }
    
    /**
     * Retrieves all draft mandates.
     * 
     * @return list of draft mandates
     */
    public List<MandateResponseDTO> getAllDrafts() {
        log.info("Retrieving all draft mandates");
        return adminApprovalService.getMandateStore().values().stream()
                .filter(mandate -> "DRAFT".equals(mandate.getStatus()))
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    /**
     * Retrieves a draft mandate by ID.
     * 
     * @param id the mandate ID
     * @return optional mandate response
     */
    public Optional<MandateResponseDTO> getDraftById(Long id) {
        log.info("Retrieving draft mandate: {}", id);
        return Optional.ofNullable(adminApprovalService.getMandateStore().get(id))
                .filter(mandate -> "DRAFT".equals(mandate.getStatus()))
                .map(this::mapToResponseDTO);
    }
    
    /**
     * Submits a draft mandate for approval.
     * Changes status from DRAFT to PENDING_APPROVAL.
     * 
     * @param id the mandate ID
     * @return updated mandate response
     * @throws IllegalArgumentException if mandate not found or not in draft status
     */
    public MandateResponseDTO submitForApproval(Long id) {
        log.info("Submitting draft {} for approval", id);
        
        Mandate mandate = adminApprovalService.getMandateStore().get(id);
        if (mandate == null) {
            throw new IllegalArgumentException("Mandate not found with ID: " + id);
        }
        
        if (!"DRAFT".equals(mandate.getStatus())) {
            throw new IllegalArgumentException("Can only submit mandates in DRAFT status");
        }
        
        mandate.setStatus("PENDING_APPROVAL");
        mandate.setUpdatedAt(LocalDateTime.now());
        adminApprovalService.getMandateStore().put(mandate.getId(), mandate);
        
        log.info("Draft {} submitted for approval", id);
        return mapToResponseDTO(mandate);
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
