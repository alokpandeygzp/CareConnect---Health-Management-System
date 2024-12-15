package com.healthcare.payment_service;

import com.healthcare.payment_service.controllers.PaymentController;
import com.healthcare.payment_service.entities.Payment;
import com.healthcare.payment_service.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment() {
        // Arrange
        Payment mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setAmount(1000.0);
        mockPayment.setStatus("SUCCESS");

        when(paymentService.processPayment(any(Payment.class))).thenReturn(mockPayment);

        // Act
        ResponseEntity<Payment> response = paymentController.processPayment(mockPayment);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPayment, response.getBody());
        verify(paymentService, times(1)).processPayment(any(Payment.class));
    }

    @Test
    void testGetPayment() {
        // Arrange
        Long paymentId = 1L;
        Payment mockPayment = new Payment();
        mockPayment.setId(paymentId);
        mockPayment.setAmount(1000.0);
        mockPayment.setStatus("SUCCESS");

        when(paymentService.getPayment(paymentId)).thenReturn(mockPayment);

        // Act
        ResponseEntity<Payment> response = paymentController.getPayment(paymentId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockPayment, response.getBody());
        verify(paymentService, times(1)).getPayment(paymentId);
    }
}
