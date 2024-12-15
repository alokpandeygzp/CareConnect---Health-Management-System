package com.healthcare.payment_service.services;

import com.healthcare.payment_service.entities.Payment;

public interface PaymentService {
    Payment processPayment(Payment payment);
    Payment getPayment(Long paymentId);
}
