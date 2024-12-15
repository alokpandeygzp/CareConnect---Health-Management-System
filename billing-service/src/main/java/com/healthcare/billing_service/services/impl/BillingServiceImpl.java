package com.healthcare.billing_service.services.impl;

import com.healthcare.billing_service.entities.Billing;
import com.healthcare.billing_service.exceptions.ApiException;
import com.healthcare.billing_service.exceptions.ResourceNotFoundException;
import com.healthcare.billing_service.repositories.BillingRepository;
import com.healthcare.billing_service.services.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BillingRepository billingRepository;

    private static final String USER_SERVICE_URL = "http://USER-SERVICE/api/users/";
    private static final String APPOINTMENT_SERVICE_URL = "http://APPOINTMENT-SERVICE/api/appointments/";

    @Override
    public Billing createBilling(Billing billing) {
        // Validate Patient ID
        if (!isValidPatient(billing.getPatientId())) {
            throw new IllegalArgumentException("Invalid Patient ID.");
        }

        // Validate Appointment ID
        if (!isValidAppointment(billing.getAppointmentId())) {
            throw new IllegalArgumentException("Invalid Appointment ID.");
        }

        // Save Billing Record
        return billingRepository.save(billing);
    }

    @Override
    public Billing getBillingById(Long id) {
        return billingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Billing","id",id));
    }

    public boolean isValidPatient(Long patientId) {
        String url = USER_SERVICE_URL + patientId;
        try {
            restTemplate.getForObject(url, Object.class); // Assuming User Service returns valid data
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Patient", "id", patientId);
        } catch (Exception e) {
            throw new ApiException("Error communicating with User Service.");
        }
    }

    public boolean isValidAppointment(Long appointmentId) {
        String url = APPOINTMENT_SERVICE_URL + appointmentId;
        try {
            restTemplate.getForObject(url, Object.class); // Assuming Appointment Service returns valid data
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Appointment", "id",appointmentId);
        }
        catch(Exception e) {
            throw new ApiException("Error communicating with Appointment Service.");
        }
    }

}
