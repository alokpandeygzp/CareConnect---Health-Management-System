package com.healthcare.billing_service;

import com.healthcare.billing_service.entities.Billing;
import com.healthcare.billing_service.exceptions.ApiException;
import com.healthcare.billing_service.exceptions.ResourceNotFoundException;
import com.healthcare.billing_service.repositories.BillingRepository;
import com.healthcare.billing_service.services.impl.BillingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingServiceImplTest {

    @InjectMocks
    private BillingServiceImpl billingService;

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private RestTemplate restTemplate;

    private Billing mockBilling;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockBilling = new Billing();
        mockBilling.setId(1L);
        mockBilling.setPatientId(100L);
        mockBilling.setAppointmentId(200L);
        mockBilling.setTotalAmount(500.0);
    }

    @Test
    void testCreateBilling_Success() {
        // Arrange
        when(restTemplate.getForObject("http://USER-SERVICE/api/users/100", Object.class))
                .thenReturn(new Object()); // Simulating valid patient response
        when(restTemplate.getForObject("http://APPOINTMENT-SERVICE/api/appointments/200", Object.class))
                .thenReturn(new Object()); // Simulating valid appointment response
        when(billingRepository.save(mockBilling)).thenReturn(mockBilling);

        // Act
        Billing result = billingService.createBilling(mockBilling);

        // Assert
        assertNotNull(result);
        assertEquals(mockBilling.getTotalAmount(), result.getTotalAmount());
        verify(billingRepository, times(1)).save(mockBilling);
    }

    @Test
    void testCreateBilling_InvalidPatient() {
        // Arrange
        when(restTemplate.getForObject("http://USER-SERVICE/api/users/100", Object.class))
                .thenThrow(HttpClientErrorException.NotFound.class); // Simulating invalid patient

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> billingService.createBilling(mockBilling));
        assertTrue(exception.getMessage().contains("Patient"));
        verify(billingRepository, never()).save(mockBilling);
    }

    @Test
    void testCreateBilling_InvalidAppointment() {
        // Arrange
        when(restTemplate.getForObject("http://USER-SERVICE/api/users/100", Object.class))
                .thenReturn(new Object()); // Simulating valid patient response
        when(restTemplate.getForObject("http://APPOINTMENT-SERVICE/api/appointments/200", Object.class))
                .thenThrow(HttpClientErrorException.NotFound.class); // Simulating invalid appointment

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> billingService.createBilling(mockBilling));
        assertTrue(exception.getMessage().contains("Appointment"));
        verify(billingRepository, never()).save(mockBilling);
    }

    @Test
    void testGetBillingById_Success() {
        // Arrange
        when(billingRepository.findById(1L)).thenReturn(Optional.of(mockBilling));

        // Act
        Billing result = billingService.getBillingById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(mockBilling.getId(), result.getId());
        verify(billingRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBillingById_NotFound() {
        // Arrange
        when(billingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> billingService.getBillingById(1L));
        assertTrue(exception.getMessage().contains("Billing"));
        verify(billingRepository, times(1)).findById(1L);
    }
}
