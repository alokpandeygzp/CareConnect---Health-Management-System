package com.healthcare.appointment_service.services.impl;

import com.healthcare.appointment_service.entities.Appointment;
import com.healthcare.appointment_service.entities.DoctorAvailability;
import com.healthcare.appointment_service.entities.User;
import com.healthcare.appointment_service.exceptions.ApiException;
import com.healthcare.appointment_service.exceptions.ResourceNotFoundException;
import com.healthcare.appointment_service.repositories.AppointmentRepository;
import com.healthcare.appointment_service.repositories.DoctorAvailabilityRepository;
import com.healthcare.appointment_service.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Autowired
    private RestTemplate restTemplate;



    public String getUserRole(int userId) {
        // Define the User Service endpoint to check the user's role
        String userRoleUrl = "http://USER-SERVICE/api/users/" + userId + "/role";

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




    @Override
    public Appointment bookAppointment(Appointment appointment) {
        String userRole = getUserRole(appointment.getDoctorId());

        if ("DOCTOR_USER".equalsIgnoreCase(userRole)) {
            // Check doctor availability
            DoctorAvailability availability = availabilityRepository
                    .findByDoctorIdAndDate(appointment.getDoctorId(), appointment.getDate())
                    .orElseThrow(() -> new ApiException("No availability found for the doctor on the given date"));

            if (!availability.getAvailableTimeSlots().contains(appointment.getTimeSlot())) {
                throw new ApiException("Time slot not available");
            }

            // Remove booked slot
            availability.getAvailableTimeSlots().remove(appointment.getTimeSlot());
            availabilityRepository.save(availability);

            // Save appointment
            appointment.setStatus("BOOKED");
            return appointmentRepository.save(appointment);
        }
        else {
            System.out.println("User is not a doctor");
            throw new ApiException("User is not a doctor");
        }
    }

    @Override
    public Appointment getAppointmentById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ApiException("Appointment not found"));
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Integer doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Integer patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    public Appointment updateAppointment(Integer appointmentId, Appointment updatedAppointment) {
        Appointment existing = getAppointmentById(appointmentId);
        existing.setDate(updatedAppointment.getDate());
        existing.setTimeSlot(updatedAppointment.getTimeSlot());
        existing.setReason(updatedAppointment.getReason());
        return appointmentRepository.save(existing);
    }

    @Override
    public void cancelAppointment(Integer appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

    @Override
    public DoctorAvailability setDoctorAvailability(DoctorAvailability availability) {
        try {
            // Fetch the user role using the user ID
            String userRole = getUserRole(availability.getDoctorId());

            // Check if the user role is "DOCTOR"
            if ("DOCTOR_USER".equalsIgnoreCase(userRole)) {
                // Proceed with saving the availability if the user is a doctor
                return availabilityRepository.save(availability);
            } else {
                throw new ApiException("The user is not a doctor. Cannot set availability.");
            }
        } catch (ResourceNotFoundException e) {
            // Log and propagate the ResourceNotFoundException
            System.out.println("Error occurred while validating doctor: " + e.getMessage());
            throw e;
        } catch (ApiException e) {
            // Log and propagate the ApiException
            System.out.println("Error occurred: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Catch unexpected errors, log them, and throw a generic API exception
            String errorMessage = "An unexpected error occurred while setting doctor availability: " + e.getMessage();
            System.out.println(errorMessage);
            throw new ApiException(errorMessage);
        }
    }


    @Override
    public DoctorAvailability getDoctorAvailability(Integer doctorId, LocalDate date) {
        return availabilityRepository.findByDoctorIdAndDate(doctorId, date)
                .orElseThrow(() -> new ApiException("Doctor availability not found"));
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByDate(date);
    }
}
