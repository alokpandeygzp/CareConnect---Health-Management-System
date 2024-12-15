package com.healthcare.appointment_service.services;

import com.healthcare.appointment_service.entities.Appointment;
import com.healthcare.appointment_service.entities.DoctorAvailability;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    Appointment bookAppointment(Appointment appointment);
    Appointment getAppointmentById(Integer appointmentId);
    List<Appointment> getAppointmentsByDoctor(Integer doctorId, LocalDate date);
    List<Appointment> getAppointmentsByPatient(Integer patientId);
    Appointment updateAppointment(Integer appointmentId, Appointment updatedAppointment);
    void cancelAppointment(Integer appointmentId);

    DoctorAvailability setDoctorAvailability(DoctorAvailability availability);
    DoctorAvailability getDoctorAvailability(Integer doctorId, LocalDate date);

    List<Appointment> getAppointmentsByDate(LocalDate date);
}
