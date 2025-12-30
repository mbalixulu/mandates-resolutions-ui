package com.example.mandates.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating or updating a Mandate request.
 * Used to transfer data from the client to the server.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandateRequestDTO {
    
    @NotBlank(message = "Company registration is required")
    private String companyRegistration;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @NotBlank(message = "Created by is required")
    private String createdBy;
}
