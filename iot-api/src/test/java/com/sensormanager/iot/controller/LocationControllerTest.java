package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.service.LocationService;

class LocationControllerTest {

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("null")
    @Test
    void testFindAll_ReturnsLocations() {
        List<LocationDTO> locations = Arrays.asList(
            new LocationDTO(1L, "Location1","EEUU","Los Angeles","Street","1222",true,1743172352L,1L), 
            new LocationDTO(2L, "Location2", "EEUU","Los Angeles","Street","1222",true,1743172352L,1L));
        when(locationService.findAll()).thenReturn(locations);
        ResponseEntity<List<LocationDTO>> response = locationController.findAll();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(locationService, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsNoContent() {
        when(locationService.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<LocationDTO>> response = locationController.findAll();
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
        verify(locationService, times(1)).findAll();
    }

    @SuppressWarnings("null")
    @Test
    void testFindById_ReturnsLocation() {
        LocationDTO location = new LocationDTO(1L, "Location1","EEUU","Los Angeles","Street","1222",true,1743172352L,1L);
        when(locationService.findById(1L)).thenReturn(location);
        ResponseEntity<LocationDTO> response = locationController.findById(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).findById(1L);
    }

    @Test
    void testFindById_ReturnsNotFound() {
        LocationDTO location = new LocationDTO();
        when(locationService.findById(1L)).thenReturn(location);
        ResponseEntity<LocationDTO> response = locationController.findById(1L);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        verify(locationService, times(1)).findById(1L);
    }

    @SuppressWarnings("null")
    @Test
    void testCreate_ReturnsCreatedLocation() {
        LocationDTO location = new LocationDTO(null, "New Location","EEUU","Los Angeles","Street","1222",true,1743172352L,1L);
        LocationDTO createdLocation = new LocationDTO(1L, "New Location","Canada","Canada","Street","1222",true,1743172352L,1L);
        when(locationService.create(location)).thenReturn(createdLocation);
        ResponseEntity<LocationDTO> response = locationController.create(location);
        assertEquals( HttpStatusCode.valueOf(200)  , response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).create(location);
    }

    @Test
    void testCreate_ReturnsBadRequest() {
        LocationDTO location = new LocationDTO(null, "Invalid Location", "EEUU","Los Angeles","Street","1222",true,1743172352L,1L);
        when(locationService.create(location)).thenReturn(location);
        ResponseEntity<LocationDTO> response = locationController.create(location);
        assertEquals( HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(locationService, times(1)).create(location);
    }

    @SuppressWarnings("null")
    @Test
    void testUpdate_ReturnsUpdatedLocation() {
        LocationDTO location = new LocationDTO(1L, "Updated Location","EEUU","Los Angeles","Street","1222",true,1743172352L,1L);
        when(locationService.update(location)).thenReturn(location);
        ResponseEntity<LocationDTO> response = locationController.update(location);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).update(location);
    }

    @Test
    void testUpdate_ReturnsBadRequest() {
        LocationDTO location = new LocationDTO(null, "Invalid Location","EEUU","Los Angeles","Street","1222",true,1743172352L,1L);
        when(locationService.update(location)).thenReturn(location);
        ResponseEntity<LocationDTO> response = locationController.update(location);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(locationService, times(1)).update(location);
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteById_ReturnsDeletedLocation() {
        LocationDTO location = new LocationDTO(1L, "Deleted Location","EEUU","Los Angeles","Street","1222",true,1743172352L,1L);
        when(locationService.deleteById(1L)).thenReturn(location);
        ResponseEntity<LocationDTO> response = locationController.deleteById(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(locationService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ReturnsBadRequest() {
        LocationDTO location = new LocationDTO();
        when(locationService.deleteById(1L)).thenReturn(location);
        ResponseEntity<LocationDTO> response = locationController.deleteById(1L);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        verify(locationService, times(1)).deleteById(1L);
    }
}