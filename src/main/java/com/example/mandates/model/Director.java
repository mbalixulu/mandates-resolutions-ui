package com.example.mandates.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a Director in the system.
 * Directors are individuals authorized to act on behalf of a company.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String email;
    private String companyRegistration;
    private boolean validated;
}
