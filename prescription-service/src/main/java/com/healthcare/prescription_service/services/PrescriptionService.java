package com.healthcare.prescription_service.services;

import com.healthcare.prescription_service.entities.Prescription;

import java.util.List;
import java.util.Optional;

public interface PrescriptionService {
    Prescription createPrescription(Prescription prescription);
    Prescription getPrescriptionById(Long id);
    List<Prescription> getPrescriptionsByPatientId(Integer patientId);
}
