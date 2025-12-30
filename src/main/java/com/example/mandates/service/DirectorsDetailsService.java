package com.example.mandates.service;

import com.example.mandates.dto.DirectorValidationDTO;
import com.example.mandates.model.Director;
import com.example.mandates.util.StringUtils;
import com.example.mandates.util.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service class for Directors Details operations.
 * Handles business logic related to director validation and management.
 * This service is responsible for:
 * - Validating director information
 * - Storing validated directors
 * - Retrieving director details
 */
@Slf4j
@Service
public class DirectorsDetailsService {
    
    private final Map<Long, Director> directorStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    /**
     * Validates and stores director information.
     * 
     * @param validationDTO the director validation data
     * @return true if validation successful and director stored, false otherwise
     */
    public boolean validateAndStoreDirector(DirectorValidationDTO validationDTO) {
        log.info("Validating director: {} {}", validationDTO.getFirstName(), validationDTO.getLastName());
        
        // Validate ID number
        if (!ValidationUtils.isValidIdNumber(validationDTO.getIdNumber())) {
            log.warn("Invalid ID number format: {}", validationDTO.getIdNumber());
            return false;
        }
        
        // Validate email
        if (!ValidationUtils.isValidEmail(validationDTO.getEmail())) {
            log.warn("Invalid email format: {}", validationDTO.getEmail());
            return false;
        }
        
        // Validate and normalize registration
        String normalizedReg = StringUtils.normalizeRegistration(validationDTO.getCompanyRegistration());
        if (!ValidationUtils.isValidRegistration(normalizedReg)) {
            log.warn("Invalid company registration format: {}", validationDTO.getCompanyRegistration());
            return false;
        }
        
        // Check if director already exists (by ID number)
        Optional<Director> existingDirector = directorStore.values().stream()
                .filter(d -> d.getIdNumber().equals(validationDTO.getIdNumber()))
                .findFirst();
        
        if (existingDirector.isPresent()) {
            log.info("Director already exists with ID number: {}", validationDTO.getIdNumber());
            return true; // Already validated
        }
        
        // Create and store new director
        Director director = Director.builder()
                .id(idGenerator.getAndIncrement())
                .firstName(StringUtils.capitalizeWords(validationDTO.getFirstName()))
                .lastName(StringUtils.capitalizeWords(validationDTO.getLastName()))
                .idNumber(validationDTO.getIdNumber())
                .email(validationDTO.getEmail().toLowerCase())
                .companyRegistration(normalizedReg)
                .validated(true)
                .build();
        
        directorStore.put(director.getId(), director);
        log.info("Director validated and stored with ID: {}", director.getId());
        
        return true;
    }
    
    /**
     * Retrieves a director by ID.
     * 
     * @param id the director ID
     * @return optional director
     */
    public Optional<Director> getDirectorById(Long id) {
        log.info("Retrieving director with ID: {}", id);
        return Optional.ofNullable(directorStore.get(id));
    }
    
    /**
     * Retrieves all validated directors.
     * 
     * @return list of all validated directors
     */
    public List<Director> getAllValidatedDirectors() {
        log.info("Retrieving all validated directors");
        return directorStore.values().stream()
                .filter(Director::isValidated)
                .toList();
    }
    
    /**
     * Retrieves directors by company registration.
     * 
     * @param companyRegistration the company registration number
     * @return list of directors for the company
     */
    public List<Director> getDirectorsByCompany(String companyRegistration) {
        log.info("Retrieving directors for company: {}", companyRegistration);
        
        String normalizedReg = StringUtils.normalizeRegistration(companyRegistration);
        
        return directorStore.values().stream()
                .filter(d -> d.getCompanyRegistration().equals(normalizedReg))
                .toList();
    }
    
    /**
     * Checks if a director with the given ID number is validated.
     * 
     * @param idNumber the ID number to check
     * @return true if director is validated, false otherwise
     */
    public boolean isDirectorValidated(String idNumber) {
        return directorStore.values().stream()
                .anyMatch(d -> d.getIdNumber().equals(idNumber) && d.isValidated());
    }
}
