package com.example.mandates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Mandate response data.
 * Used to transfer mandate data from the server to the client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandateResponseDTO {
    
    private Long id;
    private String companyRegistration;
    private String title;
    private String description;
    private String status;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String approvedBy;
    private LocalDateTime approvedAt;
}
