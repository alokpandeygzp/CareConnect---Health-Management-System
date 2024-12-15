package com.healthcare.appointment_service.controllers;

import com.healthcare.appointment_service.entities.Appointment;
import com.healthcare.appointment_service.entities.DoctorAvailability;
import com.healthcare.appointment_service.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;


    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.bookAppointment(appointment));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Integer appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }

    @GetMapping("/by-doctor")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(
            @RequestParam Integer doctorId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId, date));
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Integer appointmentId, @RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.updateAppointment(appointmentId, appointment));
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Integer appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/availability")
    public ResponseEntity<DoctorAvailability> setDoctorAvailability(@RequestBody DoctorAvailability availability) {
        DoctorAvailability doctorAvailability = appointmentService.setDoctorAvailability(availability);
        return ResponseEntity.ok(doctorAvailability);
    }

    @GetMapping("/availability")
    public ResponseEntity<DoctorAvailability> getDoctorAvailability(
            @RequestParam Integer doctorId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDoctorAvailability(doctorId, date));
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }
}
