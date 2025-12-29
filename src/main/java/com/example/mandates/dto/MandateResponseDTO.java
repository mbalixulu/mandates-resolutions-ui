package com.example.mandates.dto;

import com.example.mandates.model.Mandate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Mandate responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandateResponseDTO {
    
    private Long id;
    private String mandateNumber;
    private String companyRegistration;
    private String companyName;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    
    /**
     * Converts a Mandate model to a MandateResponseDTO.
     */
    public static MandateResponseDTO fromModel(Mandate mandate) {
        return MandateResponseDTO.builder()
                .id(mandate.getId())
                .mandateNumber(mandate.getMandateNumber())
                .companyRegistration(mandate.getCompanyRegistration())
                .companyName(mandate.getCompanyName())
                .status(mandate.getStatus())
                .createdDate(mandate.getCreatedDate())
                .modifiedDate(mandate.getModifiedDate())
                .build();
    }
}
