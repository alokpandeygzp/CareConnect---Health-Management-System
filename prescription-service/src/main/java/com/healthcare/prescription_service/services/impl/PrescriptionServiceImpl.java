package com.healthcare.prescription_service.services.impl;

import com.healthcare.prescription_service.entities.Prescription;
import com.healthcare.prescription_service.exceptions.ApiException;
import com.healthcare.prescription_service.exceptions.ResourceNotFoundException;
import com.healthcare.prescription_service.repositories.PrescriptionRepository;
import com.healthcare.prescription_service.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Prescription createPrescription(Prescription prescription) {
        // Validate doctor role
        String doctorRole = getUserRole(prescription.getDoctorId());
        System.out.println(doctorRole);
        if (!"DOCTOR_USER".equalsIgnoreCase(doctorRole)) {
            throw new ApiException("User with ID " + prescription.getDoctorId() + " is not a doctor.");
        }

        // Validate patient role
        String patientRole = getUserRole(prescription.getPatientId());
        System.out.println(patientRole);
        if (!"NORMAL_USER".equalsIgnoreCase(patientRole)) {
            throw new ApiException("User with ID " + prescription.getPatientId() + " is not a patient.");
        }
        // Associate medications with the prescription
        if (prescription.getMedications() != null) {
            prescription.getMedications().forEach(medication -> medication.setPrescription(prescription));
        }

        // Save prescription
        return prescriptionRepository.save(prescription);
    }

    /**
     * Retrieves a prescription by its ID along with its medications.
     */
    @Override
    public Prescription getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findByIdWithMedications(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", "id", id));
        prescription.getMedications().size(); // Force initialization of medications
        return prescription;
    }

    /**
     * Retrieves prescriptions by patient ID.
     */
    @Override
    public List<Prescription> getPrescriptionsByPatientId(Integer patientId) {
        // Validate that the user is a patient before fetching prescriptions
        String patientRole = getUserRole(patientId);
        if (!"NORMAL_USER".equalsIgnoreCase(patientRole)) {
            throw new ApiException("User with ID " + patientId + " is not a patient.");
        }

        return prescriptionRepository.findByPatientId(patientId);
    }

    public String getUserRole(int userId) {
        // Define the User Service endpoint to check the user's role
        String userRoleUrl = "http://localhost:8080/api/users/" + userId + "/role";

        try {
            // Make a GET request to the User Service to get the role of the user
            ResponseEntity<String> response = restTemplate.exchange(userRoleUrl, HttpMethod.GET, null, String.class);
            return response.getBody(); // This will return the role (e.g., "DOCTOR", "PATIENT")
        } catch (HttpClientErrorException e) {
            // Capture the error response body from the microservice
//            String errorMessage = e.getResponseBodyAsString();
            throw new ResourceNotFoundException("User", "userId", userId);
        } catch (Exception e) {
            // Log the exception message from unexpected errors
            throw new ApiException("Error occurred while checking user role: " + e.getMessage());
        }
    }
}
