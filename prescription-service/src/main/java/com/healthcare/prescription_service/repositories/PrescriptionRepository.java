package com.healthcare.prescription_service.repositories;

import com.healthcare.prescription_service.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    List<Prescription> findByPatientId(Integer patientId);

    @Query("SELECT p FROM Prescription p LEFT JOIN FETCH p.medications WHERE p.id = :id")
    Optional<Prescription> findByIdWithMedications(@Param("id") Long id);

}
