package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.service.LocationService;

class LocationControllerTest {

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_ReturnsListOfLocations() {
        List<LocationDTO> mockLocations = Arrays.asList(
            new LocationDTO(1L, "Location 1","Chile","Location Name 1","1","Location Street",false,1742433289L, 1L), 
            new LocationDTO(2L, "Location 2","Chile","Location Name 2","1","Location Street",false,1742433289L, 1L));
        when(locationService.findAll()).thenReturn(mockLocations);

        ResponseEntity<List<LocationDTO>> response = locationController.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        //////////////////////////////////////////////////////////////////////////////////////////////////
        
        //////////////////////////////////////////////////////////////////////////////////////////////////
        verify(locationService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsNoContent() {
        when(locationService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<LocationDTO>> response = locationController.findAll();

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(locationService, times(1)).findAll();
    }

    @Test
    void testFindById_ReturnsLocation() {
        LocationDTO mockLocation = new LocationDTO(1L, "Location","Chile","Location Name","1","Location Street",false,1742433289L, 1L);
        when(locationService.findById(1L)).thenReturn(mockLocation);

        ResponseEntity<LocationDTO> response = locationController.findById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).findById(1L);
    }

    @Test
    void testFindById_ThrowsNotFoundException() {
        when(locationService.findById(1L)).thenReturn(new LocationDTO());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> locationController.findById(1L));

        assertEquals(404, exception.getStatusCode().value());
        assertEquals("The location ID: 1 does not exist.", exception.getReason());
        verify(locationService, times(1)).findById(1L);
    }

    @Test
    void testCreate_ReturnsInsertedLocation() {
        LocationDTO inputLocation = new LocationDTO(1L, "New Location","Chile","Location Name","1","Location Street",false,1742433289L, 1L);
        LocationDTO mockInsertedLocation = new LocationDTO(1L, "New Location","Chile","Location Name","1","Location Street",false,1742433289L, 1L);
        when(locationService.create(inputLocation)).thenReturn(mockInsertedLocation);

        ResponseEntity<LocationDTO> response = locationController.create(inputLocation);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).create(inputLocation);
    }

    @Test
    void testCreate_ThrowsBadRequestException() {
        LocationDTO inputLocation = new LocationDTO(1L, "Location","Chile","Location Name","1","Location Street",false,1742433289L, 1L);
        when(locationService.create(inputLocation)).thenReturn(new LocationDTO());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> locationController.create(inputLocation));

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("The location was not inserted.", exception.getReason());
        verify(locationService, times(1)).create(inputLocation);
    }

    @Test
    void testUpdate_ReturnsUpdatedLocation() {
        LocationDTO inputLocation = new LocationDTO(1L, "Location","Chile","Location Name","1","Location Street",false,1742433289L, 1L);
        when(locationService.update(inputLocation)).thenReturn(inputLocation);

        ResponseEntity<LocationDTO> response = locationController.update(inputLocation);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Location", response.getBody().getLocationName());
        verify(locationService, times(1)).update(inputLocation);
    }

    @Test
    void testUpdate_ThrowsBadRequestException() {
        LocationDTO inputLocation = new LocationDTO(1L, "Location Delete","Chile","Location Name","1","Location Street",false,1742433289L, 1L);
        when(locationService.update(inputLocation)).thenReturn(new LocationDTO());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> locationController.update(inputLocation));

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("The location was not updated.", exception.getReason());
        verify(locationService, times(1)).update(inputLocation);
    }

    @Test
    void testDeleteById_ReturnsDeletedLocation() {
        LocationDTO mockDeletedLocation = new LocationDTO(1L, "Location Delete","Chile","Location Name","1","Location Street",false,1742433289L, 1L);
        when(locationService.deleteById(1L)).thenReturn(mockDeletedLocation);

        ResponseEntity<LocationDTO> response = locationController.deleteById(1L);

        assertEquals(200L, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ThrowsBadRequestException() {
        when(locationService.deleteById(1L)).thenReturn(new LocationDTO());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> locationController.deleteById(1L));

        assertEquals(400, exception.getStatusCode().value());
        assertEquals("The location was not disabled.", exception.getReason());
        verify(locationService, times(1)).deleteById(1L);
    }
}