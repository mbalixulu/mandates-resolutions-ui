package com.example.mandates.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing a Director.
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
    private boolean validated;
}
