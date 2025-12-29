package com.example.mandates.service;

import com.example.mandates.dto.DirectorValidationDTO;
import com.example.mandates.dto.MandateRequestDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.model.Director;
import com.example.mandates.model.Mandate;
import com.example.mandates.util.StringUtils;
import com.example.mandates.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer for Mandates and Resolutions business logic.
 * This class contains all business logic extracted from MandatesResolutionUIController.
 * It acts as an intermediary between the Controller and data persistence layers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MandatesResolutionService {
    
    private final ValidationUtils validationUtils;
    private final StringUtils stringUtils;
    
    // In-memory storage for demonstration purposes
    private final ConcurrentHashMap<Long, Mandate> mandateStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Director> directorStore = new ConcurrentHashMap<>();
    private final AtomicLong mandateIdGenerator = new AtomicLong(1);
    private final AtomicLong directorIdGenerator = new AtomicLong(1);
    
    /**
     * Processes a new mandate request.
     * Business logic for creating a mandate with validation.
     * 
     * @param requestDTO the mandate request data
     * @return created mandate response
     * @throws IllegalArgumentException if validation fails
     */
    public MandateResponseDTO processRequest(MandateRequestDTO requestDTO) {
        log.info("Processing mandate request for company: {}", requestDTO.getCompanyName());
        
        // Normalize and validate company registration
        String normalizedReg = stringUtils.normalizeRegistration(requestDTO.getCompanyRegistration());
        if (!validationUtils.isValidRegistration(normalizedReg)) {
            throw new IllegalArgumentException("Invalid company registration number: " + normalizedReg);
        }
        
        // Deduplicate director IDs if provided
        String directorIds = requestDTO.getDirectorIds();
        if (directorIds != null && !directorIds.isEmpty()) {
            directorIds = stringUtils.deduplicateCommaString(directorIds);
        }
        
        // Create mandate
        Mandate mandate = Mandate.builder()
                .id(mandateIdGenerator.getAndIncrement())
                .mandateNumber(requestDTO.getMandateNumber())
                .companyRegistration(normalizedReg)
                .companyName(stringUtils.capitalizeWords(requestDTO.getCompanyName()))
                .status("PENDING")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
        
        mandateStore.put(mandate.getId(), mandate);
        
        log.info("Created mandate with ID: {}", mandate.getId());
        return MandateResponseDTO.fromModel(mandate);
    }
    
    /**
     * Handles director validation logic.
     * Validates director information and stores if valid.
     * 
     * @param validationDTO the director validation data
     * @return validation result
     */
    public boolean handleDirectorValidation(DirectorValidationDTO validationDTO) {
        log.info("Validating director: {} {}", validationDTO.getFirstName(), validationDTO.getLastName());
        
        // Validate ID number
        if (!validationUtils.isValidIdNumber(validationDTO.getIdNumber())) {
            log.warn("Invalid ID number: {}", validationDTO.getIdNumber());
            return false;
        }
        
        // Validate email
        if (!validationUtils.isValidEmail(validationDTO.getEmail())) {
            log.warn("Invalid email: {}", validationDTO.getEmail());
            return false;
        }
        
        // Create and store director
        Director director = Director.builder()
                .id(directorIdGenerator.getAndIncrement())
                .firstName(stringUtils.capitalizeWords(validationDTO.getFirstName()))
                .lastName(stringUtils.capitalizeWords(validationDTO.getLastName()))
                .idNumber(validationDTO.getIdNumber())
                .email(validationDTO.getEmail().toLowerCase())
                .validated(true)
                .build();
        
        directorStore.put(director.getId(), director);
        
        log.info("Director validated and stored with ID: {}", director.getId());
        return true;
    }
    
    /**
     * Retrieves a mandate by ID.
     * 
     * @param id the mandate ID
     * @return optional mandate response
     */
    public Optional<MandateResponseDTO> getMandateById(Long id) {
        log.debug("Retrieving mandate with ID: {}", id);
        return Optional.ofNullable(mandateStore.get(id))
                .map(MandateResponseDTO::fromModel);
    }
    
    /**
     * Retrieves all mandates.
     * 
     * @return list of all mandates
     */
    public List<MandateResponseDTO> getAllMandates() {
        log.debug("Retrieving all mandates");
        return mandateStore.values().stream()
                .map(MandateResponseDTO::fromModel)
                .toList();
    }
    
    /**
     * Updates a mandate status.
     * 
     * @param id the mandate ID
     * @param status the new status
     * @return updated mandate response
     * @throws IllegalArgumentException if mandate not found
     */
    public MandateResponseDTO updateMandateStatus(Long id, String status) {
        log.info("Updating mandate {} status to: {}", id, status);
        
        Mandate mandate = mandateStore.get(id);
        if (mandate == null) {
            throw new IllegalArgumentException("Mandate not found with ID: " + id);
        }
        
        mandate.setStatus(status);
        mandate.setModifiedDate(LocalDateTime.now());
        
        return MandateResponseDTO.fromModel(mandate);
    }
    
    /**
     * Searches mandates by company registration.
     * 
     * @param registration the company registration number
     * @return list of matching mandates
     */
    public List<MandateResponseDTO> searchByRegistration(String registration) {
        log.debug("Searching mandates by registration: {}", registration);
        
        String normalizedReg = stringUtils.normalizeRegistration(registration);
        
        return mandateStore.values().stream()
                .filter(m -> m.getCompanyRegistration().equals(normalizedReg))
                .map(MandateResponseDTO::fromModel)
                .toList();
    }
    
    /**
     * Retrieves a director by ID.
     * 
     * @param id the director ID
     * @return optional director
     */
    public Optional<Director> getDirectorById(Long id) {
        log.debug("Retrieving director with ID: {}", id);
        return Optional.ofNullable(directorStore.get(id));
    }
    
    /**
     * Retrieves all validated directors.
     * 
     * @return list of validated directors
     */
    public List<Director> getValidatedDirectors() {
        log.debug("Retrieving all validated directors");
        return directorStore.values().stream()
                .filter(Director::isValidated)
                .toList();
    }
}
