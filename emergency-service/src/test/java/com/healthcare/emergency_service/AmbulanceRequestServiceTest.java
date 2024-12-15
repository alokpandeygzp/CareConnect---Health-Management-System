package com.healthcare.emergency_service;

import com.healthcare.emergency_service.entities.AmbulanceRequest;
import com.healthcare.emergency_service.exceptions.ResourceNotFoundException;
import com.healthcare.emergency_service.repositories.AmbulanceRequestRepository;
import com.healthcare.emergency_service.services.AmbulanceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmbulanceRequestServiceTest {

    @InjectMocks
    private AmbulanceRequestService requestService;

    @Mock
    private AmbulanceRequestRepository requestRepository;

    private AmbulanceRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRequest = new AmbulanceRequest();
        mockRequest.setId(1);
        mockRequest.setPatientName("John Doe");
        mockRequest.setStatus("PENDING");
        mockRequest.setRequestTime(LocalDateTime.now());
    }

    @Test
    void testCreateRequest() {
        // Arrange
        when(requestRepository.save(mockRequest)).thenReturn(mockRequest);

        // Act
        AmbulanceRequest createdRequest = requestService.createRequest(mockRequest);

        // Assert
        assertNotNull(createdRequest);
        assertEquals("PENDING", createdRequest.getStatus());
        verify(requestRepository, times(1)).save(mockRequest);
    }

    @Test
    void testUpdateRequestStatus_NotFound() {
        // Arrange
        when(requestRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> requestService.updateRequestStatus(1, "COMPLETED"));
        assertTrue(exception.getMessage().contains("AmbulanceRequest"));
        verify(requestRepository, times(1)).findById(1);
    }

    @Test
    void testGetRequestsByStatus() {
        // Arrange
        when(requestRepository.findByStatus("PENDING")).thenReturn(List.of(mockRequest));

        // Act
        List<AmbulanceRequest> requests = requestService.getRequestsByStatus("PENDING");

        // Assert
        assertNotNull(requests);
        assertEquals(1, requests.size());
        verify(requestRepository, times(1)).findByStatus("PENDING");
    }
}
