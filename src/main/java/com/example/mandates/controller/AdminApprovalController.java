package com.example.mandates.controller;

import com.example.mandates.dto.AdminApprovalDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.service.AdminApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Admin Approval operations.
 * This controller handles HTTP requests for mandate approval/rejection workflows.
 * 
 * Responsibilities:
 * - HTTP request/response handling
 * - Input validation
 * - Delegating business logic to AdminApprovalService
 * - Returning appropriate HTTP status codes
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/approvals")
@RequiredArgsConstructor
public class AdminApprovalController {
    
    private final AdminApprovalService adminApprovalService;
    
    /**
     * Processes an approval or rejection request.
     * 
     * @param approvalDTO the approval/rejection details
     * @return updated mandate response with 200 OK
     */
    @PostMapping
    public ResponseEntity<MandateResponseDTO> processApproval(
            @Valid @RequestBody AdminApprovalDTO approvalDTO) {
        log.info("Received approval/rejection request for mandate ID: {}", approvalDTO.getMandateId());
        
        try {
            MandateResponseDTO response = adminApprovalService.processApproval(approvalDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error processing approval: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Retrieves all mandates pending approval.
     * 
     * @return list of pending mandates with 200 OK
     */
    @GetMapping("/pending")
    public ResponseEntity<List<MandateResponseDTO>> getPendingApprovals() {
        log.info("Received request to get all pending approvals");
        
        List<MandateResponseDTO> pendingApprovals = adminApprovalService.getPendingApprovals();
        return ResponseEntity.ok(pendingApprovals);
    }
    
    /**
     * Retrieves a specific mandate for approval review.
     * 
     * @param id the mandate ID
     * @return mandate response with 200 OK, or 404 NOT FOUND if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<MandateResponseDTO> getMandateForApproval(@PathVariable Long id) {
        log.info("Received request to get mandate for approval: {}", id);
        
        return adminApprovalService.getMandateForApproval(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
