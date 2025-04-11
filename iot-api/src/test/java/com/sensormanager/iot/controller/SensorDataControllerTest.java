package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.service.SensorDataService;

class SensorDataControllerTest {

    @Mock
    private SensorDataService sensorDataService;

    @InjectMocks
    private SensorDataController sensorDataController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSensorData() {
        List<SensorDataDTO> mockData = Arrays.asList(new SensorDataDTO(), new SensorDataDTO());
        when(sensorDataService.getAllSensorData()).thenReturn(mockData);
        ResponseEntity<List<SensorDataDTO>> response = sensorDataController.getAllSensorData();
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockData, response.getBody());
        verify(sensorDataService, times(1)).getAllSensorData();
    }

    @Test
    void testGetSensorDataById_Found() {
        SensorDataDTO mockData = new SensorDataDTO();
        when(sensorDataService.getSensorDataById(1L)).thenReturn(mockData);
        ResponseEntity<SensorDataDTO> response = sensorDataController.getSensorDataById(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockData, response.getBody());
        verify(sensorDataService, times(1)).getSensorDataById(1L);
    }

    @Test
    void testGetSensorDataById_NotFound() {
        when(sensorDataService.getSensorDataById(1L)).thenReturn(null);
        ResponseEntity<SensorDataDTO> response = sensorDataController.getSensorDataById(1L);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNull(response.getBody());
        verify(sensorDataService, times(1)).getSensorDataById(1L);
    }
  /*  @Disabled
    @Test
    void testCreateSensorData() {
        SensorDataDTO inputData = new SensorDataDTO();
        SensorDataDTO mockData = new SensorDataDTO();
        when(sensorDataService.createSensorData(inputData)).thenReturn(mockData);
        ResponseEntity<SensorDataDTO> response = sensorDataController.createSensorData(inputData);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockData, response.getBody());
        verify(sensorDataService, times(1)).createSensorData(inputData);
    }*/

    @Test
    void testUpdateSensorData_Found() {
        SensorDataDTO inputData = new SensorDataDTO();
        SensorDataDTO mockData = new SensorDataDTO();
        when(sensorDataService.updateSensorData(1L, inputData)).thenReturn(mockData);
        ResponseEntity<SensorDataDTO> response = sensorDataController.updateSensorData(1L, inputData);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockData, response.getBody());
        verify(sensorDataService, times(1)).updateSensorData(1L, inputData);
    }

    @Test
    void testUpdateSensorData_NotFound() {
        SensorDataDTO inputData = new SensorDataDTO();
        when(sensorDataService.updateSensorData(1L, inputData)).thenReturn(null);
        ResponseEntity<SensorDataDTO> response = sensorDataController.updateSensorData(1L, inputData);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertNull(response.getBody());
        verify(sensorDataService, times(1)).updateSensorData(1L, inputData);
    }

    @Test
    void testDeleteSensorData() {
        doNothing().when(sensorDataService).deleteSensorData(1L);
        ResponseEntity<Void> response = sensorDataController.deleteSensorData(1L);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
        assertNull(response.getBody());
        verify(sensorDataService, times(1)).deleteSensorData(1L);
    }
}