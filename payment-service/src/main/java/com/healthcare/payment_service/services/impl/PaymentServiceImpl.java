package com.healthcare.payment_service.services.impl;

import com.healthcare.payment_service.entities.Payment;
import com.healthcare.payment_service.exceptions.ApiException;
import com.healthcare.payment_service.exceptions.ResourceNotFoundException;
import com.healthcare.payment_service.repositories.PaymentRepository;
import com.healthcare.payment_service.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;


    private static final String BILLING_SERVICE_URL = "http://BILLING-SERVICE/billing/";

    @Override
    public Payment processPayment(Payment payment) {

        if (!isValidBill(payment.getBillingId())) {
            throw new IllegalArgumentException("Invalid Billing ID.");
        }

//        payment.setStatus("Processed");
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(()-> new ResourceNotFoundException("Payment","id",paymentId));
    }


    public boolean isValidBill(Long billId) {
        String url = BILLING_SERVICE_URL + billId;
        try {
            restTemplate.getForObject(url, Object.class); // Assuming Billing Service returns valid data
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Billing", "id", billId);
        } catch (Exception e) {
            throw new ApiException("Error communicating with Billing Service.");
        }
    }
}
