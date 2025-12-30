package com.example.mandates.service;

import com.example.mandates.dto.AdminApprovalDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.model.Mandate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service class for Admin Approval operations.
 * Handles business logic related to approving or rejecting mandates.
 * This service is responsible for:
 * - Processing approval requests
 * - Processing rejection requests
 * - Retrieving pending approvals
 */
@Slf4j
@Service
public class AdminApprovalService {
    
    private final Map<Long, Mandate> mandateStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    /**
     * Processes an approval or rejection request for a mandate.
     * 
     * @param approvalDTO the approval/rejection details
     * @return updated mandate response
     * @throws IllegalArgumentException if mandate not found or action is invalid
     */
    public MandateResponseDTO processApproval(AdminApprovalDTO approvalDTO) {
        log.info("Processing approval for mandate ID: {}", approvalDTO.getMandateId());
        
        Mandate mandate = getMandateEntity(approvalDTO.getMandateId())
                .orElseThrow(() -> new IllegalArgumentException("Mandate not found with ID: " + approvalDTO.getMandateId()));
        
        if (!"PENDING_APPROVAL".equals(mandate.getStatus())) {
            throw new IllegalArgumentException("Mandate is not in pending approval status");
        }
        
        String action = approvalDTO.getAction().toUpperCase();
        if ("APPROVE".equals(action)) {
            mandate.setStatus("APPROVED");
            mandate.setApprovedBy(approvalDTO.getApproverName());
            mandate.setApprovedAt(LocalDateTime.now());
            log.info("Mandate {} approved by {}", mandate.getId(), approvalDTO.getApproverName());
        } else if ("REJECT".equals(action)) {
            mandate.setStatus("REJECTED");
            mandate.setApprovedBy(approvalDTO.getApproverName());
            mandate.setApprovedAt(LocalDateTime.now());
            log.info("Mandate {} rejected by {}", mandate.getId(), approvalDTO.getApproverName());
        } else {
            throw new IllegalArgumentException("Invalid action. Must be APPROVE or REJECT");
        }
        
        mandate.setUpdatedAt(LocalDateTime.now());
        saveMandate(mandate);
        
        return mapToResponseDTO(mandate);
    }
    
    /**
     * Retrieves all mandates pending approval.
     * 
     * @return list of mandates with PENDING_APPROVAL status
     */
    public List<MandateResponseDTO> getPendingApprovals() {
        log.info("Retrieving all pending approvals");
        return getAllMandateEntities().stream()
                .filter(mandate -> "PENDING_APPROVAL".equals(mandate.getStatus()))
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    /**
     * Retrieves a mandate by ID for approval review.
     * 
     * @param id the mandate ID
     * @return optional mandate response
     */
    public Optional<MandateResponseDTO> getMandateForApproval(Long id) {
        log.info("Retrieving mandate for approval: {}", id);
        return getMandateEntity(id)
                .filter(mandate -> "PENDING_APPROVAL".equals(mandate.getStatus()))
                .map(this::mapToResponseDTO);
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
    
    /**
     * Generates the next unique ID for a mandate.
     * 
     * @return the next available ID
     */
    public Long generateNextId() {
        return idGenerator.getAndIncrement();
    }
    
    /**
     * Saves a mandate to the data store.
     * 
     * @param mandate the mandate to save
     */
    public void saveMandate(Mandate mandate) {
        mandateStore.put(mandate.getId(), mandate);
    }
    
    /**
     * Retrieves a mandate by ID.
     * 
     * @param id the mandate ID
     * @return optional mandate
     */
    public Optional<Mandate> getMandateEntity(Long id) {
        return Optional.ofNullable(mandateStore.get(id));
    }
    
    /**
     * Retrieves all mandates as entities.
     * 
     * @return list of all mandates
     */
    public List<Mandate> getAllMandateEntities() {
        return List.copyOf(mandateStore.values());
    }
}
