package com.example.mandates.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating or updating a Mandate.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandateRequestDTO {
    
    @NotBlank(message = "Mandate number is required")
    private String mandateNumber;
    
    @NotBlank(message = "Company registration is required")
    private String companyRegistration;
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    private String directorIds;
}
