package com.healthcare.mailing_service.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentNotificationService {

    private final EmailService emailService;
    private final RestTemplate restTemplate;

    private static final String APPOINTMENT_SERVICE_URL = "http://APPOINTMENT-SERVICE/api/appointments/by-date";

    public AppointmentNotificationService(EmailService emailService, RestTemplate restTemplate) {
        this.emailService = emailService;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Kolkata")  // Run at midnight every day
    public void notifyUpcomingAppointments() {
        System.out.println("Appointment notification task started.");

        // Get tomorrow's date
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Fetch appointments for tomorrow
        String url = APPOINTMENT_SERVICE_URL + "?date=" + tomorrow.format(formatter);
        try {
            List<Map<String, Object>> appointments = restTemplate.getForObject(url, List.class);

            if (appointments != null && !appointments.isEmpty()) {
                for (Map<String, Object> appointment : appointments) {
                    Integer patientId = (Integer) appointment.get("patientId");
                    Integer doctorId = (Integer) appointment.get("doctorId");
                    String timeSlot = (String) appointment.get("timeSlot");
                    String reason = (String) appointment.get("reason");

                    // Fetch patient and doctor details (pseudo-code)
                    String patientEmail = getUserEmail(patientId);
                    String doctorEmail = getUserEmail(doctorId);

                    // Email content
                    String subject = "Reminder: Upcoming Appointment on " + tomorrow;
                    String body = String.format("""
                            Dear User,
                            
                            This is a reminder about your upcoming appointment:
                            - Date: %s
                            - Time: %s
                            - Reason: %s
                            
                            Please ensure you are prepared.
                            
                            Best regards,
                            Health Management System
                            """, tomorrow, timeSlot, reason);

                    // Send emails
                    if (patientEmail != null) {
                        emailService.sendEmail(patientEmail, subject, body);
                        System.out.println("Email sent to patient: " + patientEmail);
                    }
                    if (doctorEmail != null) {
                        emailService.sendEmail(doctorEmail, subject, body);
                        System.out.println("Email sent to doctor: " + doctorEmail);
                    }
                }
            } else {
                System.out.println("No appointments for tomorrow.");
            }
        } catch (Exception e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
        }
    }

    private String getUserEmail(Integer userId) {
        // Fetch user details from User Service (pseudo-code)
        String url = "http://USER-SERVICE/api/users/" + userId;

        try {
            Map<String, Object> userDetails = restTemplate.getForObject(url, Map.class);
            return userDetails != null ? (String) userDetails.get("email") : null;
        } catch (Exception e) {
            System.err.println("Error fetching user email: " + e.getMessage());
            return null;
        }
    }
}
