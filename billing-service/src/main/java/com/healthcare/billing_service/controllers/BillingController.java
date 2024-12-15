package com.healthcare.billing_service.controllers;

import com.healthcare.billing_service.entities.Billing;
import com.healthcare.billing_service.services.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping
    public ResponseEntity<Billing> createBilling(@RequestBody Billing billing) {
        return ResponseEntity.ok(billingService.createBilling(billing));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillingById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillingById(id));
    }
}
