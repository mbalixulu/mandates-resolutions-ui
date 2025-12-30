package com.example.mandates.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for validating director information.
 * Used when validating director credentials.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectorValidationDTO {
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "ID number is required")
    private String idNumber;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Company registration is required")
    private String companyRegistration;
}
