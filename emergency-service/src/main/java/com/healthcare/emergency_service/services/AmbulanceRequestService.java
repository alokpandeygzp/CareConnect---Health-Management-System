package com.healthcare.emergency_service.services;

import com.healthcare.emergency_service.entities.AmbulanceRequest;
import com.healthcare.emergency_service.exceptions.ResourceNotFoundException;
import com.healthcare.emergency_service.repositories.AmbulanceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AmbulanceRequestService {

    @Autowired
    private AmbulanceRequestRepository requestRepository;

    public AmbulanceRequest createRequest(AmbulanceRequest request) {
        // Set initial status and request time
        request.setStatus("PENDING");
        request.setRequestTime(LocalDateTime.now());
        return requestRepository.save(request);
    }

    public AmbulanceRequest updateRequestStatus(Integer requestId, String status) {
        // Fetch request by ID
        AmbulanceRequest request = requestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("AmbulanceRequest", "id", requestId));

        // Update the status
        request.setStatus(status);

        // If the request is completed, set the completion time
        if ("COMPLETED".equalsIgnoreCase(status)) {
            request.setCompletionTime(LocalDateTime.now());
        }

        return requestRepository.save(request);
    }

    public List<AmbulanceRequest> getRequestsByStatus(String status) {
        return requestRepository.findByStatus(status);
    }
}
