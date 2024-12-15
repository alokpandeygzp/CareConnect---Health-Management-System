package com.healthcare.appointment_service.repositories;

import com.healthcare.appointment_service.entities.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Integer> {
    Optional<DoctorAvailability> findByDoctorIdAndDate(Integer doctorId, LocalDate date);
}
