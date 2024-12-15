package com.healthcare.emergency_service.repositories;

import com.healthcare.emergency_service.entities.AmbulanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmbulanceRequestRepository extends JpaRepository<AmbulanceRequest, Integer> {
    List<AmbulanceRequest> findByStatus(String status); // Fetch requests by status
}
