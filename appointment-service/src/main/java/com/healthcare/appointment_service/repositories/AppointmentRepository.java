package com.healthcare.appointment_service.repositories;

import com.healthcare.appointment_service.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorIdAndDate(Integer doctorId, LocalDate date);
    List<Appointment> findByPatientId(Integer patientId);

    List<Appointment> findByDate(LocalDate date);
}
