package com.healthcare.appointment_service;

import com.healthcare.appointment_service.entities.Appointment;
import com.healthcare.appointment_service.entities.DoctorAvailability;
import com.healthcare.appointment_service.exceptions.ApiException;
import com.healthcare.appointment_service.repositories.AppointmentRepository;
import com.healthcare.appointment_service.repositories.DoctorAvailabilityRepository;
import com.healthcare.appointment_service.services.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorAvailabilityRepository availabilityRepository;

    @Mock
    private RestTemplate restTemplate;

    private Appointment mockAppointment;
    private DoctorAvailability mockDoctorAvailability;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock appointment and doctor availability
        mockAppointment = new Appointment();
        mockAppointment.setId(1);
        mockAppointment.setDoctorId(1001);
        mockAppointment.setPatientId(2001);
        mockAppointment.setDate(LocalDate.now());
        mockAppointment.setTimeSlot("10:00 AM");
        mockAppointment.setStatus("BOOKED");

        mockDoctorAvailability = new DoctorAvailability();
        mockDoctorAvailability.setDoctorId(1001);
        mockDoctorAvailability.setDate(LocalDate.now());
        mockDoctorAvailability.setAvailableTimeSlots(List.of("10:00 AM", "11:00 AM"));
    }

    @Test
    void testBookAppointment_NoAvailability() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("DOCTOR_USER"));
        when(availabilityRepository.findByDoctorIdAndDate(mockAppointment.getDoctorId(), mockAppointment.getDate()))
            .thenReturn(Optional.of(mockDoctorAvailability));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> appointmentService.bookAppointment(mockAppointment));
        assertEquals("Time slot not available", exception.getMessage());
    }

    @Test
    void testBookAppointment_UserNotDoctor() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("PATIENT_USER"));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> appointmentService.bookAppointment(mockAppointment));
        assertEquals("User is not a doctor", exception.getMessage());
    }

    @Test
    void testGetAppointmentById_Success() {
        // Arrange
        when(appointmentRepository.findById(mockAppointment.getId())).thenReturn(Optional.of(mockAppointment));

        // Act
        Appointment retrievedAppointment = appointmentService.getAppointmentById(mockAppointment.getId());

        // Assert
        assertNotNull(retrievedAppointment);
        assertEquals(mockAppointment.getId(), retrievedAppointment.getId());
    }

    @Test
    void testGetAppointmentById_NotFound() {
        // Arrange
        when(appointmentRepository.findById(mockAppointment.getId())).thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> appointmentService.getAppointmentById(mockAppointment.getId()));
        assertEquals("Appointment not found", exception.getMessage());
    }

    @Test
    void testSetDoctorAvailability_Success() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("DOCTOR_USER"));
        when(availabilityRepository.save(mockDoctorAvailability)).thenReturn(mockDoctorAvailability);

        // Act
        DoctorAvailability savedAvailability = appointmentService.setDoctorAvailability(mockDoctorAvailability);

        // Assert
        assertNotNull(savedAvailability);
        verify(availabilityRepository, times(1)).save(mockDoctorAvailability);
    }

    @Test
    void testSetDoctorAvailability_UserNotDoctor() {
        // Arrange
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(), eq(String.class)))
            .thenReturn(ResponseEntity.ok("PATIENT_USER"));

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> appointmentService.setDoctorAvailability(mockDoctorAvailability));
        assertEquals("The user is not a doctor. Cannot set availability.", exception.getMessage());
    }

    @Test
    void testGetDoctorAvailability_Success() {
        // Arrange
        when(availabilityRepository.findByDoctorIdAndDate(mockDoctorAvailability.getDoctorId(), mockDoctorAvailability.getDate()))
            .thenReturn(Optional.of(mockDoctorAvailability));

        // Act
        DoctorAvailability retrievedAvailability = appointmentService.getDoctorAvailability(mockDoctorAvailability.getDoctorId(), mockDoctorAvailability.getDate());

        // Assert
        assertNotNull(retrievedAvailability);
        assertEquals(mockDoctorAvailability.getDoctorId(), retrievedAvailability.getDoctorId());
    }

    @Test
    void testGetDoctorAvailability_NotFound() {
        // Arrange
        when(availabilityRepository.findByDoctorIdAndDate(mockDoctorAvailability.getDoctorId(), mockDoctorAvailability.getDate()))
            .thenReturn(Optional.empty());

        // Act & Assert
        ApiException exception = assertThrows(ApiException.class, () -> appointmentService.getDoctorAvailability(mockDoctorAvailability.getDoctorId(), mockDoctorAvailability.getDate()));
        assertEquals("Doctor availability not found", exception.getMessage());
    }
}
