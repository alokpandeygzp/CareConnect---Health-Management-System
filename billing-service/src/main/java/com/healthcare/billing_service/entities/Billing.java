package com.healthcare.billing_service.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Double totalAmount;
    private LocalDateTime billingDate;
    private String description;
    private Long appointmentId;

    public Billing(Long id, Long patientId, Double totalAmount, LocalDateTime billingDate, String description, Long appointmentId) {
        this.id = id;
        this.patientId = patientId;
        this.totalAmount = totalAmount;
        this.billingDate = billingDate;
        this.description = description;
        this.appointmentId = appointmentId;
    }
    public Billing() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDateTime billingDate) {
        this.billingDate = billingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    // Getters and Setters
}
