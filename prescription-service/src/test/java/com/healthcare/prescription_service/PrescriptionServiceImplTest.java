package com.healthcare.prescription_service;

import com.healthcare.prescription_service.entities.Prescription;
import com.healthcare.prescription_service.exceptions.ApiException;
import com.healthcare.prescription_service.exceptions.ResourceNotFoundException;
import com.healthcare.prescription_service.repositories.PrescriptionRepository;
import com.healthcare.prescription_service.services.impl.PrescriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionServiceImplTest {

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private RestTemplate restTemplate;

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
    void testCreatePrescription_InvalidDoctor() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("NORMAL_USER"));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> prescriptionService.createPrescription(mockPrescription));
        assertEquals("User with ID 1001 is not a doctor.", exception.getMessage());
    }

    @Test
    void testCreatePrescription_InvalidPatient() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("DOCTOR_USER"));
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("NORMAL_USER"));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> prescriptionService.createPrescription(mockPrescription));
        assertEquals("User with ID 2001 is not a patient.", exception.getMessage());
    }

    @Test
    void testGetPrescriptionById_Success() {
        // Arrange
        when(prescriptionRepository.findByIdWithMedications(1L)).thenReturn(Optional.of(mockPrescription));

        // Act
        Prescription retrievedPrescription = prescriptionService.getPrescriptionById(1L);

        // Assert
        assertNotNull(retrievedPrescription);
        verify(prescriptionRepository, times(1)).findByIdWithMedications(1L);
    }

    @Test
    void testGetPrescriptionById_NotFound() {
        // Arrange
        when(prescriptionRepository.findByIdWithMedications(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> prescriptionService.getPrescriptionById(1L));
        assertTrue(exception.getMessage().contains("Prescription"));
        verify(prescriptionRepository, times(1)).findByIdWithMedications(1L);
    }

    @Test
    void testGetPrescriptionsByPatientId() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("NORMAL_USER"));
        when(prescriptionRepository.findByPatientId(2001)).thenReturn(List.of(mockPrescription));

        // Act
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatientId(2001);

        // Assert
        assertNotNull(prescriptions);
        assertEquals(1, prescriptions.size());
        verify(prescriptionRepository, times(1)).findByPatientId(2001);
    }

    @Test
    void testGetPrescriptionsByPatientId_InvalidRole() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("DOCTOR_USER"));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> prescriptionService.getPrescriptionsByPatientId(2001));
        assertEquals("User with ID 2001 is not a patient.", exception.getMessage());
    }
}
