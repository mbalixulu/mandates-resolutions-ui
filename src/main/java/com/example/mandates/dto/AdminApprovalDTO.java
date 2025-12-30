package com.example.mandates.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for admin approval/rejection actions.
 * Used when an admin approves or rejects a mandate.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminApprovalDTO {
    
    @NotNull(message = "Mandate ID is required")
    private Long mandateId;
    
    @NotBlank(message = "Action is required")
    private String action; // APPROVE or REJECT
    
    @NotBlank(message = "Approver name is required")
    private String approverName;
    
    private String comments;
}
