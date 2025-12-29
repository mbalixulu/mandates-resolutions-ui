package com.example.mandates.controller;

import com.example.mandates.dto.DirectorValidationDTO;
import com.example.mandates.dto.MandateRequestDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.model.Director;
import com.example.mandates.service.MandatesResolutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Mandates and Resolutions.
 * This controller handles HTTP requests and delegates business logic to the service layer.
 * It follows the MVC pattern with clear separation of concerns.
 */
@Slf4j
@RestController
@RequestMapping("/api/mandates")
@RequiredArgsConstructor
public class MandatesResolutionController {
    
    private final MandatesResolutionService mandatesResolutionService;
    
    /**
     * Creates a new mandate.
     * 
     * @param requestDTO the mandate request data
     * @return created mandate response
     */
    @PostMapping
    public ResponseEntity<MandateResponseDTO> createMandate(@Valid @RequestBody MandateRequestDTO requestDTO) {
        log.info("Received request to create mandate");
        try {
            MandateResponseDTO response = mandatesResolutionService.processRequest(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Retrieves a mandate by ID.
     * 
     * @param id the mandate ID
     * @return mandate response
     */
    @GetMapping("/{id}")
    public ResponseEntity<MandateResponseDTO> getMandateById(@PathVariable Long id) {
        log.info("Received request to get mandate with ID: {}", id);
        return mandatesResolutionService.getMandateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves all mandates.
     * 
     * @return list of all mandates
     */
    @GetMapping
    public ResponseEntity<List<MandateResponseDTO>> getAllMandates() {
        log.info("Received request to get all mandates");
        List<MandateResponseDTO> mandates = mandatesResolutionService.getAllMandates();
        return ResponseEntity.ok(mandates);
    }
    
    /**
     * Updates a mandate status.
     * 
     * @param id the mandate ID
     * @param statusUpdate map containing the new status
     * @return updated mandate response
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<MandateResponseDTO> updateMandateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        log.info("Received request to update mandate {} status", id);
        try {
            String status = statusUpdate.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MandateResponseDTO response = mandatesResolutionService.updateMandateStatus(id, status);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error updating mandate: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Searches mandates by company registration.
     * 
     * @param registration the company registration number
     * @return list of matching mandates
     */
    @GetMapping("/search")
    public ResponseEntity<List<MandateResponseDTO>> searchByRegistration(
            @RequestParam String registration) {
        log.info("Received request to search mandates by registration: {}", registration);
        List<MandateResponseDTO> mandates = mandatesResolutionService.searchByRegistration(registration);
        return ResponseEntity.ok(mandates);
    }
    
    /**
     * Validates a director.
     * 
     * @param validationDTO the director validation data
     * @return validation result
     */
    @PostMapping("/directors/validate")
    public ResponseEntity<Map<String, Boolean>> validateDirector(
            @Valid @RequestBody DirectorValidationDTO validationDTO) {
        log.info("Received request to validate director");
        boolean isValid = mandatesResolutionService.handleDirectorValidation(validationDTO);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
    
    /**
     * Retrieves a director by ID.
     * 
     * @param id the director ID
     * @return director information
     */
    @GetMapping("/directors/{id}")
    public ResponseEntity<Director> getDirectorById(@PathVariable Long id) {
        log.info("Received request to get director with ID: {}", id);
        return mandatesResolutionService.getDirectorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retrieves all validated directors.
     * 
     * @return list of validated directors
     */
    @GetMapping("/directors")
    public ResponseEntity<List<Director>> getValidatedDirectors() {
        log.info("Received request to get all validated directors");
        List<Director> directors = mandatesResolutionService.getValidatedDirectors();
        return ResponseEntity.ok(directors);
    }
}
