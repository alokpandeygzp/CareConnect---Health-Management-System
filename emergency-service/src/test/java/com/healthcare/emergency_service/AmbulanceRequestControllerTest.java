package com.healthcare.emergency_service;

import com.healthcare.emergency_service.controllers.AmbulanceRequestController;
import com.healthcare.emergency_service.entities.AmbulanceRequest;
import com.healthcare.emergency_service.exceptions.ResourceNotFoundException;
import com.healthcare.emergency_service.services.AmbulanceRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AmbulanceRequestControllerTest {

    @InjectMocks
    private AmbulanceRequestController requestController;

    @Mock
    private AmbulanceRequestService requestService;

    private AmbulanceRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockRequest = new AmbulanceRequest();
        mockRequest.setId(1);
        mockRequest.setPatientName("John Doe");
        mockRequest.setStatus("PENDING");
    }

    @Test
    void testCreateRequest() {
        // Arrange
        when(requestService.createRequest(mockRequest)).thenReturn(mockRequest);

        // Act
        ResponseEntity<AmbulanceRequest> response = requestController.createRequest(mockRequest);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("PENDING", response.getBody().getStatus());
        verify(requestService, times(1)).createRequest(mockRequest);
    }

    @Test
    void testUpdateRequestStatus_Success() {
        // Arrange
        when(requestService.updateRequestStatus(1, "COMPLETED")).thenReturn(mockRequest);

        // Act
        ResponseEntity<AmbulanceRequest> response = requestController.updateRequestStatus(1, "COMPLETED");

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("COMPLETED", response.getBody().getStatus());
        verify(requestService, times(1)).updateRequestStatus(1, "COMPLETED");
    }

    @Test
    void testUpdateRequestStatus_NotFound() {
        // Arrange
        when(requestService.updateRequestStatus(1, "COMPLETED")).thenThrow(new ResourceNotFoundException("AmbulanceRequest", "id", 1));

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> requestController.updateRequestStatus(1, "COMPLETED"));
        assertTrue(exception.getMessage().contains("AmbulanceRequest"));
        verify(requestService, times(1)).updateRequestStatus(1, "COMPLETED");
    }

    @Test
    void testGetRequestsByStatus() {
        // Arrange
        when(requestService.getRequestsByStatus("PENDING")).thenReturn(List.of(mockRequest));

        // Act
        ResponseEntity<List<AmbulanceRequest>> response = requestController.getRequestsByStatus("PENDING");

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(requestService, times(1)).getRequestsByStatus("PENDING");
    }
}
