package com.healthcare.prescription_service.repositories;

import com.healthcare.prescription_service.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

}
