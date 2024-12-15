package com.healthcare.billing_service;

import com.healthcare.billing_service.controllers.BillingController;
import com.healthcare.billing_service.entities.Billing;
import com.healthcare.billing_service.services.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BillingControllerTest {

    @InjectMocks
    private BillingController billingController;

    @Mock
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBilling() {
        // Arrange
        Billing mockBilling = new Billing();
        mockBilling.setId(1L);
        mockBilling.setTotalAmount(200.0);
        mockBilling.setDescription("Test Billing");

        when(billingService.createBilling(mockBilling)).thenReturn(mockBilling);

        // Act
        ResponseEntity<Billing> response = billingController.createBilling(mockBilling);

        // Assert
        assertEquals(200.0, response.getBody().getTotalAmount());
        assertEquals("Test Billing", response.getBody().getDescription());
        verify(billingService, times(1)).createBilling(mockBilling);
    }

    @Test
    void testGetBillingById_Success() {
        // Arrange
        Long billingId = 1L;
        Billing mockBilling = new Billing();
        mockBilling.setId(billingId);
        mockBilling.setTotalAmount(300.0);
        mockBilling.setDescription("Sample Billing");

        when(billingService.getBillingById(billingId)).thenReturn(mockBilling);

        // Act
        ResponseEntity<Billing> response = billingController.getBillingById(billingId);

        // Assert
        assertEquals(billingId, response.getBody().getId());
        assertEquals(300.0, response.getBody().getTotalAmount());
        verify(billingService, times(1)).getBillingById(billingId);
    }
}
