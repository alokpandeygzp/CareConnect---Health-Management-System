package com.healthcare.billing_service.services;

import com.healthcare.billing_service.entities.Billing;

import java.util.Optional;

public interface BillingService {
    Billing createBilling(Billing billing);

    Billing getBillingById(Long id);
}
