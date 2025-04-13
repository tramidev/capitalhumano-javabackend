package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LocationControllerTest {

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_ReturnsLocations() {
        List<LocationDTO> locationDTOs = Arrays.asList(
                new LocationDTO(1L, "Location1", "USA", "LA", "Street", "123", true, 1743172352L, 1L),
                new LocationDTO(2L, "Location2", "USA", "NY", "Avenue", "456", true, 1743172352L, 1L)
        );

        when(locationService.findAll()).thenReturn(locationDTOs);

        ResponseEntity<List<LocationDTO>> response = locationController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(locationService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsNoContent() {
        when(locationService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<LocationDTO>> response = locationController.findAll();

        assertEquals(204, response.getStatusCodeValue());
        verify(locationService, times(1)).findAll();
    }

    @Test
    void testFindById_ReturnsLocation() {
        LocationDTO locationDTO = new LocationDTO(1L, "Location1", "USA", "LA", "Street", "123", true, 1743172352L, 1L);
        when(locationService.findById(1L)).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.findById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).findById(1L);
    }

    @Test
    void testFindById_ReturnsNotFound() {
        when(locationService.findById(1L)).thenReturn(new LocationDTO());

        ResponseEntity<LocationDTO> response = locationController.findById(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(locationService, times(1)).findById(1L);
    }

    @Test
    void testCreate_ReturnsCreatedLocation() {
        LocationDTO requestDTO = new LocationDTO(null, "New Location", "USA", "LA", "Street", "123", true, 1743172352L, 1L);
        LocationDTO responseDTO = new LocationDTO(1L, "New Location", "USA", "LA", "Street", "123", true, 1743172352L, 1L);

        when(locationService.create(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<LocationDTO> response = locationController.create(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).create(requestDTO);
    }

    @Test
    void testCreate_ReturnsBadRequest() {
        LocationDTO requestDTO = new LocationDTO();
        when(locationService.create(requestDTO)).thenReturn(requestDTO);

        ResponseEntity<LocationDTO> response = locationController.create(requestDTO);

        assertEquals(400, response.getStatusCodeValue());
        verify(locationService, times(1)).create(requestDTO);
    }

    @Test
    void testUpdate_ReturnsUpdatedLocation() {
        LocationDTO locationDTO = new LocationDTO(1L, "Updated", "USA", "NY", "Avenue", "456", true, 1743172352L, 1L);
        when(locationService.update(locationDTO)).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.update(locationDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).update(locationDTO);
    }

    @Test
    void testUpdate_ReturnsBadRequest() {
        LocationDTO invalidDTO = new LocationDTO();
        when(locationService.update(invalidDTO)).thenReturn(invalidDTO);

        ResponseEntity<LocationDTO> response = locationController.update(invalidDTO);

        assertEquals(400, response.getStatusCodeValue());
        verify(locationService, times(1)).update(invalidDTO);
    }

    @Test
    void testDeleteById_ReturnsDeletedLocation() {
        LocationDTO locationDTO = new LocationDTO(1L, "Deleted", "USA", "LA", "Street", "123", true, 1743172352L, 1L);
        when(locationService.deleteById(1L)).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.deleteById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ReturnsBadRequest() {
        LocationDTO emptyDTO = new LocationDTO();
        when(locationService.deleteById(1L)).thenReturn(emptyDTO);

        ResponseEntity<LocationDTO> response = locationController.deleteById(1L);

        assertEquals(400, response.getStatusCodeValue());
        verify(locationService, times(1)).deleteById(1L);
    }
}
