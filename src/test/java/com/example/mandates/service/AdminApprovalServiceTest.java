package com.example.mandates.service;

import com.example.mandates.dto.AdminApprovalDTO;
import com.example.mandates.dto.MandateResponseDTO;
import com.example.mandates.model.Mandate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AdminApprovalService.
 */
class AdminApprovalServiceTest {
    
    private AdminApprovalService adminApprovalService;
    
    @BeforeEach
    void setUp() {
        adminApprovalService = new AdminApprovalService();
    }
    
    @Test
    void testProcessApproval_Approve() {
        // Setup: Create a pending mandate
        Mandate mandate = Mandate.builder()
                .id(1L)
                .companyRegistration("2023/123456/07")
                .title("Test Mandate")
                .status("PENDING_APPROVAL")
                .createdBy("user1")
                .createdAt(LocalDateTime.now())
                .build();
        adminApprovalService.getMandateStore().put(1L, mandate);
        
        // Execute approval
        AdminApprovalDTO approvalDTO = AdminApprovalDTO.builder()
                .mandateId(1L)
                .action("APPROVE")
                .approverName("admin1")
                .build();
        
        MandateResponseDTO response = adminApprovalService.processApproval(approvalDTO);
        
        // Verify
        assertNotNull(response);
        assertEquals("APPROVED", response.getStatus());
        assertEquals("admin1", response.getApprovedBy());
        assertNotNull(response.getApprovedAt());
    }
    
    @Test
    void testProcessApproval_Reject() {
        // Setup
        Mandate mandate = Mandate.builder()
                .id(2L)
                .companyRegistration("2023/123456/07")
                .title("Test Mandate")
                .status("PENDING_APPROVAL")
                .createdBy("user1")
                .createdAt(LocalDateTime.now())
                .build();
        adminApprovalService.getMandateStore().put(2L, mandate);
        
        // Execute rejection
        AdminApprovalDTO approvalDTO = AdminApprovalDTO.builder()
                .mandateId(2L)
                .action("REJECT")
                .approverName("admin1")
                .build();
        
        MandateResponseDTO response = adminApprovalService.processApproval(approvalDTO);
        
        // Verify
        assertNotNull(response);
        assertEquals("REJECTED", response.getStatus());
        assertEquals("admin1", response.getApprovedBy());
        assertNotNull(response.getApprovedAt());
    }
    
    @Test
    void testProcessApproval_MandateNotFound() {
        AdminApprovalDTO approvalDTO = AdminApprovalDTO.builder()
                .mandateId(999L)
                .action("APPROVE")
                .approverName("admin1")
                .build();
        
        assertThrows(IllegalArgumentException.class, 
                () -> adminApprovalService.processApproval(approvalDTO));
    }
    
    @Test
    void testProcessApproval_InvalidStatus() {
        // Setup mandate not in pending status
        Mandate mandate = Mandate.builder()
                .id(3L)
                .companyRegistration("2023/123456/07")
                .title("Test Mandate")
                .status("DRAFT")
                .createdBy("user1")
                .createdAt(LocalDateTime.now())
                .build();
        adminApprovalService.getMandateStore().put(3L, mandate);
        
        AdminApprovalDTO approvalDTO = AdminApprovalDTO.builder()
                .mandateId(3L)
                .action("APPROVE")
                .approverName("admin1")
                .build();
        
        assertThrows(IllegalArgumentException.class, 
                () -> adminApprovalService.processApproval(approvalDTO));
    }
    
    @Test
    void testProcessApproval_InvalidAction() {
        Mandate mandate = Mandate.builder()
                .id(4L)
                .companyRegistration("2023/123456/07")
                .title("Test Mandate")
                .status("PENDING_APPROVAL")
                .createdBy("user1")
                .createdAt(LocalDateTime.now())
                .build();
        adminApprovalService.getMandateStore().put(4L, mandate);
        
        AdminApprovalDTO approvalDTO = AdminApprovalDTO.builder()
                .mandateId(4L)
                .action("INVALID")
                .approverName("admin1")
                .build();
        
        assertThrows(IllegalArgumentException.class, 
                () -> adminApprovalService.processApproval(approvalDTO));
    }
    
    @Test
    void testGetPendingApprovals() {
        // Setup multiple mandates with different statuses
        Mandate mandate1 = Mandate.builder()
                .id(1L)
                .status("PENDING_APPROVAL")
                .build();
        Mandate mandate2 = Mandate.builder()
                .id(2L)
                .status("APPROVED")
                .build();
        Mandate mandate3 = Mandate.builder()
                .id(3L)
                .status("PENDING_APPROVAL")
                .build();
        
        adminApprovalService.getMandateStore().put(1L, mandate1);
        adminApprovalService.getMandateStore().put(2L, mandate2);
        adminApprovalService.getMandateStore().put(3L, mandate3);
        
        List<MandateResponseDTO> pending = adminApprovalService.getPendingApprovals();
        
        assertEquals(2, pending.size());
    }
    
    @Test
    void testGetMandateForApproval_Found() {
        Mandate mandate = Mandate.builder()
                .id(1L)
                .status("PENDING_APPROVAL")
                .title("Test")
                .build();
        adminApprovalService.getMandateStore().put(1L, mandate);
        
        Optional<MandateResponseDTO> result = adminApprovalService.getMandateForApproval(1L);
        
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getTitle());
    }
    
    @Test
    void testGetMandateForApproval_NotFound() {
        Optional<MandateResponseDTO> result = adminApprovalService.getMandateForApproval(999L);
        assertFalse(result.isPresent());
    }
    
    @Test
    void testGetMandateForApproval_WrongStatus() {
        Mandate mandate = Mandate.builder()
                .id(1L)
                .status("DRAFT")
                .title("Test")
                .build();
        adminApprovalService.getMandateStore().put(1L, mandate);
        
        Optional<MandateResponseDTO> result = adminApprovalService.getMandateForApproval(1L);
        assertFalse(result.isPresent());
    }
}
