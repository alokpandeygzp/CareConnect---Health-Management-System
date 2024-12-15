package com.healthcare.prescription_service;

import com.healthcare.prescription_service.controllers.PrescriptionController;
import com.healthcare.prescription_service.entities.Prescription;
import com.healthcare.prescription_service.services.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionControllerTest {

    @InjectMocks
    private PrescriptionController prescriptionController;

    @Mock
    private PrescriptionService prescriptionService;

    private Prescription mockPrescription;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockPrescription = new Prescription();
        mockPrescription.setId(1L);
        mockPrescription.setDoctorId(1001);
        mockPrescription.setPatientId(2001);
        mockPrescription.setMedications(List.of());
    }

    @Test
    void testCreatePrescription() {
        // Arrange
        when(prescriptionService.createPrescription(mockPrescription)).thenReturn(mockPrescription);

        // Act
        ResponseEntity<Prescription> response = prescriptionController.createPrescription(mockPrescription);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        verify(prescriptionService, times(1)).createPrescription(mockPrescription);
    }

    @Test
    void testGetPrescriptionById_Success() {
        // Arrange
        when(prescriptionService.getPrescriptionById(1L)).thenReturn(mockPrescription);

        // Act
        ResponseEntity<Prescription> response = prescriptionController.getPrescriptionById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        verify(prescriptionService, times(1)).getPrescriptionById(1L);
    }

    @Test
    void testGetPrescriptionsByPatientId() {
        // Arrange
        when(prescriptionService.getPrescriptionsByPatientId(2001)).thenReturn(List.of(mockPrescription));

        // Act
        ResponseEntity<List<Prescription>> response = prescriptionController.getPrescriptionsByPatientId(2001);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(prescriptionService, times(1)).getPrescriptionsByPatientId(2001);
    }
}
