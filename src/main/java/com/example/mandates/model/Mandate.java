package com.example.mandates.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Model class representing a Mandate entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mandate {
    
    private Long id;
    private String mandateNumber;
    private String companyRegistration;
    private String companyName;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
