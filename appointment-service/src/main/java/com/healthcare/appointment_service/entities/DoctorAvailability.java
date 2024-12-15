package com.healthcare.appointment_service.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class DoctorAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer doctorId;
    private LocalDate date;

    @ElementCollection
    private List<String> availableTimeSlots; // Example: ["10:00 AM", "10:30 AM"]


    public DoctorAvailability(Integer id, Integer doctorId, LocalDate date, List<String> availableTimeSlots) {
        this.id = id;
        this.doctorId = doctorId;
        this.date = date;
        this.availableTimeSlots = availableTimeSlots;
    }
    public DoctorAvailability() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<String> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
}
