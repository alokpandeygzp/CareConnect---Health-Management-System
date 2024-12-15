package com.healthcare.payment_service.repositories;

import com.healthcare.payment_service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
