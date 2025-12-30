package com.example.mandates.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a Mandate in the system.
 * A Mandate represents a formal authorization or directive related to company resolutions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mandate {
    
    private Long id;
    private String companyRegistration;
    private String title;
    private String description;
    private String status; // DRAFT, PENDING_APPROVAL, APPROVED, REJECTED
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String approvedBy;
    private LocalDateTime approvedAt;
}
