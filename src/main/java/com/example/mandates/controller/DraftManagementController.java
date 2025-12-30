package com.example.mandates.controller;

import com.example.mandates.dto.MandateRequestDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.service.DraftManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Draft Management operations.
 * This controller handles HTTP requests for creating and managing draft mandates.
 * 
 * Responsibilities:
 * - HTTP request/response handling
 * - Input validation
 * - Delegating business logic to DraftManagementService
 * - Returning appropriate HTTP status codes
 */
@Slf4j
@RestController
@RequestMapping("/api/drafts")
@RequiredArgsConstructor
public class DraftManagementController {
    
    private final DraftManagementService draftManagementService;
    
    /**
     * Creates a new draft mandate.
     * 
     * @param requestDTO the mandate request data
     * @return created mandate response with 201 CREATED
     */
    @PostMapping
    public ResponseEntity<MandateResponseDTO> createDraft(
            @Valid @RequestBody MandateRequestDTO requestDTO) {
        log.info("Received request to create draft mandate");
        
        try {
            MandateResponseDTO response = draftManagementService.createDraft(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error creating draft: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Updates an existing draft mandate.
     * 
     * @param id the mandate ID
     * @param requestDTO the updated mandate data
     * @return updated mandate response with 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<MandateResponseDTO> updateDraft(
            @PathVariable Long id,
            @Valid @RequestBody MandateRequestDTO requestDTO) {
        log.info("Received request to update draft mandate: {}", id);
        
        try {
            MandateResponseDTO response = draftManagementService.updateDraft(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating draft: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Retrieves all draft mandates.
     * 
     * @return list of draft mandates with 200 OK
     */
    @GetMapping
    public ResponseEntity<List<MandateResponseDTO>> getAllDrafts() {
        log.info("Received request to get all draft mandates");
        
        List<MandateResponseDTO> drafts = draftManagementService.getAllDrafts();
        return ResponseEntity.ok(drafts);
    }
    
    /**
     * Retrieves a draft mandate by ID.
     * 
     * @param id the mandate ID
     * @return mandate response with 200 OK, or 404 NOT FOUND if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<MandateResponseDTO> getDraftById(@PathVariable Long id) {
        log.info("Received request to get draft mandate: {}", id);
        
        return draftManagementService.getDraftById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Submits a draft mandate for approval.
     * 
     * @param id the mandate ID
     * @return updated mandate response with 200 OK
     */
    @PostMapping("/{id}/submit")
    public ResponseEntity<MandateResponseDTO> submitForApproval(@PathVariable Long id) {
        log.info("Received request to submit draft {} for approval", id);
        
        try {
            MandateResponseDTO response = draftManagementService.submitForApproval(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error submitting draft: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
