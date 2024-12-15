package com.healthcare.appointment_service;

import com.healthcare.appointment_service.controllers.AppointmentController;
import com.healthcare.appointment_service.entities.Appointment;
import com.healthcare.appointment_service.entities.DoctorAvailability;
import com.healthcare.appointment_service.services.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentService;

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
    void testBookAppointment() {
        // Arrange
        when(appointmentService.bookAppointment(mockAppointment)).thenReturn(mockAppointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.bookAppointment(mockAppointment);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("BOOKED", response.getBody().getStatus());
        verify(appointmentService, times(1)).bookAppointment(mockAppointment);
    }

    @Test
    void testGetAppointmentById() {
        // Arrange
        when(appointmentService.getAppointmentById(mockAppointment.getId())).thenReturn(mockAppointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.getAppointmentById(mockAppointment.getId());

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAppointment.getId(), response.getBody().getId());
        verify(appointmentService, times(1)).getAppointmentById(mockAppointment.getId());
    }

    @Test
    void testGetAppointmentsByDoctor() {
        // Arrange
        when(appointmentService.getAppointmentsByDoctor(mockAppointment.getDoctorId(), mockAppointment.getDate()))
            .thenReturn(List.of(mockAppointment));

        // Act
        ResponseEntity<List<Appointment>> response = appointmentController.getAppointmentsByDoctor(mockAppointment.getDoctorId(), mockAppointment.getDate());

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(appointmentService, times(1)).getAppointmentsByDoctor(mockAppointment.getDoctorId(), mockAppointment.getDate());
    }

    @Test
    void testUpdateAppointment() {
        // Arrange
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setDate(LocalDate.now().plusDays(1));
        updatedAppointment.setTimeSlot("11:00 AM");

        when(appointmentService.updateAppointment(mockAppointment.getId(), updatedAppointment)).thenReturn(updatedAppointment);

        // Act
        ResponseEntity<Appointment> response = appointmentController.updateAppointment(mockAppointment.getId(), updatedAppointment);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedAppointment.getTimeSlot(), response.getBody().getTimeSlot());
        verify(appointmentService, times(1)).updateAppointment(mockAppointment.getId(), updatedAppointment);
    }

    @Test
    void testCancelAppointment() {
        // Arrange
        doNothing().when(appointmentService).cancelAppointment(mockAppointment.getId());

        // Act
        ResponseEntity<Void> response = appointmentController.cancelAppointment(mockAppointment.getId());

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(appointmentService, times(1)).cancelAppointment(mockAppointment.getId());
    }

    @Test
    void testSetDoctorAvailability() {
        // Arrange
        when(appointmentService.setDoctorAvailability(mockDoctorAvailability)).thenReturn(mockDoctorAvailability);

        // Act
        ResponseEntity<DoctorAvailability> response = appointmentController.setDoctorAvailability(mockDoctorAvailability);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(appointmentService, times(1)).setDoctorAvailability(mockDoctorAvailability);
    }
}
