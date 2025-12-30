package com.example.mandates.controller;

import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.service.MandateSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Mandate Search operations.
 * This controller handles HTTP requests for searching and filtering mandates.
 * 
 * Responsibilities:
 * - HTTP request/response handling
 * - Input validation
 * - Delegating business logic to MandateSearchService
 * - Returning appropriate HTTP status codes
 */
@Slf4j
@RestController
@RequestMapping("/api/search/mandates")
@RequiredArgsConstructor
public class MandateSearchController {
    
    private final MandateSearchService mandateSearchService;
    
    /**
     * Retrieves all mandates.
     * 
     * @return list of all mandates with 200 OK
     */
    @GetMapping
    public ResponseEntity<List<MandateResponseDTO>> getAllMandates() {
        log.info("Received request to get all mandates");
        
        List<MandateResponseDTO> mandates = mandateSearchService.getAllMandates();
        return ResponseEntity.ok(mandates);
    }
    
    /**
     * Searches mandates by company registration.
     * 
     * @param registration the company registration number
     * @return list of matching mandates with 200 OK
     */
    @GetMapping("/by-registration")
    public ResponseEntity<List<MandateResponseDTO>> searchByRegistration(
            @RequestParam String registration) {
        log.info("Received request to search mandates by registration: {}", registration);
        
        try {
            List<MandateResponseDTO> mandates = mandateSearchService.searchByRegistration(registration);
            return ResponseEntity.ok(mandates);
        } catch (IllegalArgumentException e) {
            log.error("Invalid registration: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Searches mandates by status.
     * 
     * @param status the mandate status
     * @return list of mandates with the specified status with 200 OK
     */
    @GetMapping("/by-status")
    public ResponseEntity<List<MandateResponseDTO>> searchByStatus(@RequestParam String status) {
        log.info("Received request to search mandates by status: {}", status);
        
        try {
            List<MandateResponseDTO> mandates = mandateSearchService.searchByStatus(status);
            return ResponseEntity.ok(mandates);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Searches mandates by creator.
     * 
     * @param createdBy the username of the creator
     * @return list of mandates created by the user with 200 OK
     */
    @GetMapping("/by-creator")
    public ResponseEntity<List<MandateResponseDTO>> searchByCreator(@RequestParam String createdBy) {
        log.info("Received request to search mandates by creator: {}", createdBy);
        
        try {
            List<MandateResponseDTO> mandates = mandateSearchService.searchByCreator(createdBy);
            return ResponseEntity.ok(mandates);
        } catch (IllegalArgumentException e) {
            log.error("Invalid creator: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Performs advanced search with multiple criteria.
     * 
     * @param registration optional company registration
     * @param status optional status
     * @param createdBy optional creator name
     * @return list of mandates matching all provided criteria with 200 OK
     */
    @GetMapping("/advanced")
    public ResponseEntity<List<MandateResponseDTO>> advancedSearch(
            @RequestParam(required = false) String registration,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String createdBy) {
        log.info("Received advanced search request");
        
        List<MandateResponseDTO> mandates = mandateSearchService.advancedSearch(
                registration, status, createdBy);
        return ResponseEntity.ok(mandates);
    }
}
