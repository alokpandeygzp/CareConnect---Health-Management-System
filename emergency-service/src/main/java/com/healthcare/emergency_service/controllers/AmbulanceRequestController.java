package com.healthcare.emergency_service.controllers;

import com.healthcare.emergency_service.entities.AmbulanceRequest;
import com.healthcare.emergency_service.services.AmbulanceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulance-requests")
public class AmbulanceRequestController {

    @Autowired
    private AmbulanceRequestService requestService;

    @PostMapping
    public ResponseEntity<AmbulanceRequest> createRequest(@RequestBody AmbulanceRequest request) {
        // Create a new ambulance request
        return ResponseEntity.ok(requestService.createRequest(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AmbulanceRequest> updateRequestStatus(@PathVariable Integer id, @RequestParam String status) {
        // Update the status of an existing request
        return ResponseEntity.ok(requestService.updateRequestStatus(id, status));
    }

    @GetMapping("/status")
    public ResponseEntity<List<AmbulanceRequest>> getRequestsByStatus(@RequestParam String status) {
        // Fetch all requests with a specific status
        return ResponseEntity.ok(requestService.getRequestsByStatus(status));
    }
}
