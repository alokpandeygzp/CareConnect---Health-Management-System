package com.healthcare.billing_service.repositories;

import com.healthcare.billing_service.entities.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long> {
}
