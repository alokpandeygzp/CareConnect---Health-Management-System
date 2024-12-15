package com.healthcare.payment_service;

import com.healthcare.payment_service.entities.Payment;
import com.healthcare.payment_service.exceptions.ApiException;
import com.healthcare.payment_service.exceptions.ResourceNotFoundException;
import com.healthcare.payment_service.repositories.PaymentRepository;
import com.healthcare.payment_service.services.impl.PaymentServiceImpl;
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

class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RestTemplate restTemplate;

    private static final String BILLING_SERVICE_URL = "http://BILLING-SERVICE/billing/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment_Success() {
        // Arrange
        Payment mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setBillingId(101L);
        mockPayment.setStatus("Pending");

        when(restTemplate.getForObject(BILLING_SERVICE_URL + mockPayment.getBillingId(), Object.class))
                .thenReturn(new Object());
        when(paymentRepository.save(mockPayment)).thenReturn(mockPayment);

        // Act
        Payment processedPayment = paymentService.processPayment(mockPayment);

        // Assert
        assertEquals("Processed", processedPayment.getStatus());
        verify(paymentRepository, times(1)).save(mockPayment);
    }

    @Test
    void testProcessPayment_InvalidBillingId() {
        // Arrange
        Payment mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setBillingId(101L);

        when(restTemplate.getForObject(BILLING_SERVICE_URL + mockPayment.getBillingId(), Object.class))
                .thenThrow(HttpClientErrorException.NotFound.class);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> paymentService.processPayment(mockPayment));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetPayment_Success() {
        // Arrange
        Long paymentId = 1L;
        Payment mockPayment = new Payment();
        mockPayment.setId(paymentId);
        mockPayment.setBillingId(101L);
        mockPayment.setStatus("Processed");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(mockPayment));

        // Act
        Payment retrievedPayment = paymentService.getPayment(paymentId);

        // Assert
        assertNotNull(retrievedPayment);
        assertEquals(paymentId, retrievedPayment.getId());
        verify(paymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void testGetPayment_NotFound() {
        // Arrange
        Long paymentId = 1L;

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPayment(paymentId));
        verify(paymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void testIsValidBill_Success() {
        // Arrange
        Long billId = 101L;
        when(restTemplate.getForObject(BILLING_SERVICE_URL + billId, Object.class))
                .thenReturn(new Object());

        // Act
        boolean isValid = paymentService.isValidBill(billId);

        // Assert
        assertTrue(isValid);
        verify(restTemplate, times(1)).getForObject(BILLING_SERVICE_URL + billId, Object.class);
    }

    @Test
    void testIsValidBill_NotFound() {
        // Arrange
        Long billId = 101L;
        when(restTemplate.getForObject(BILLING_SERVICE_URL + billId, Object.class))
                .thenThrow(HttpClientErrorException.NotFound.class);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> paymentService.isValidBill(billId));
        verify(restTemplate, times(1)).getForObject(BILLING_SERVICE_URL + billId, Object.class);
    }

    @Test
    void testIsValidBill_ApiException() {
        // Arrange
        Long billId = 101L;
        when(restTemplate.getForObject(BILLING_SERVICE_URL + billId, Object.class))
                .thenThrow(new RuntimeException("Billing service not reachable"));

        // Act & Assert
        assertThrows(ApiException.class, () -> paymentService.isValidBill(billId));
        verify(restTemplate, times(1)).getForObject(BILLING_SERVICE_URL + billId, Object.class);
    }
}
