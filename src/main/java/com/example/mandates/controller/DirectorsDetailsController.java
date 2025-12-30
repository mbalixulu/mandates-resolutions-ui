package com.example.mandates.controller;

import com.example.mandates.dto.DirectorValidationDTO;
import com.example.mandates.model.Director;
import com.example.mandates.service.DirectorsDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Directors Details operations.
 * This controller handles HTTP requests for director validation and management.
 * 
 * Responsibilities:
 * - HTTP request/response handling
 * - Input validation
 * - Delegating business logic to DirectorsDetailsService
 * - Returning appropriate HTTP status codes
 */
@Slf4j
@RestController
@RequestMapping("/api/directors")
@RequiredArgsConstructor
public class DirectorsDetailsController {
    
    private final DirectorsDetailsService directorsDetailsService;
    
    /**
     * Validates a director's information.
     * 
     * @param validationDTO the director validation data
     * @return validation result with 200 OK
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validateDirector(
            @Valid @RequestBody DirectorValidationDTO validationDTO) {
        log.info("Received request to validate director");
        
        boolean isValid = directorsDetailsService.validateAndStoreDirector(validationDTO);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
    
    /**
     * Retrieves a director by ID.
     * 
     * @param id the director ID
     * @return director information with 200 OK, or 404 NOT FOUND if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Director> getDirectorById(@PathVariable Long id) {
        log.info("Received request to get director with ID: {}", id);
        
        return directorsDetailsService.getDirectorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves all validated directors.
     * 
     * @return list of all validated directors with 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Director>> getAllValidatedDirectors() {
        log.info("Received request to get all validated directors");
        
        List<Director> directors = directorsDetailsService.getAllValidatedDirectors();
        return ResponseEntity.ok(directors);
    }
    
    /**
     * Retrieves directors by company registration.
     * 
     * @param registration the company registration number
     * @return list of directors for the company with 200 OK
     */
    @GetMapping("/company/{registration}")
    public ResponseEntity<List<Director>> getDirectorsByCompany(@PathVariable String registration) {
        log.info("Received request to get directors for company: {}", registration);
        
        try {
            List<Director> directors = directorsDetailsService.getDirectorsByCompany(registration);
            return ResponseEntity.ok(directors);
        } catch (IllegalArgumentException e) {
            log.error("Invalid company registration: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Checks if a director is validated.
     * 
     * @param idNumber the director's ID number
     * @return validation status with 200 OK
     */
    @GetMapping("/check/{idNumber}")
    public ResponseEntity<Map<String, Boolean>> checkDirectorValidation(@PathVariable String idNumber) {
        log.info("Received request to check director validation for ID: {}", idNumber);
        
        boolean isValidated = directorsDetailsService.isDirectorValidated(idNumber);
        return ResponseEntity.ok(Map.of("validated", isValidated));
    }
}
