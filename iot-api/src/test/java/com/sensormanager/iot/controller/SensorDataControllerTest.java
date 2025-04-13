package com.sensormanager.iot.controller;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

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

    @Test
    void testCreateSensorData() {
        SensorJSONPackageDTO input = SensorJSONPackageDTO.builder()
                .apiKey("82ba1908-96c7-4a7b-854c-969a5e389909")
                .jsonData(List.of(
                        Map.of("datetime", "1742861430", "temp", "24.4", "humidity", "0.5"),
                        Map.of("datetime", "1742861495", "temp", "22.1", "humidity", "0.6")
                ))
                .build();

        List<SensorDataDTO> mockResponse = Arrays.asList(
                SensorDataDTO.builder().id(1L).idSensor(99L).metric("temp").record("24.4").recordCreatedAt(1742861430L).build(),
                SensorDataDTO.builder().id(2L).idSensor(99L).metric("humidity").record("0.5").recordCreatedAt(1742861430L).build(),
                SensorDataDTO.builder().id(3L).idSensor(99L).metric("temp").record("22.1").recordCreatedAt(1742861495L).build(),
                SensorDataDTO.builder().id(4L).idSensor(99L).metric("humidity").record("0.6").recordCreatedAt(1742861495L).build()
        );

        when(sensorDataService.createSensorData(input)).thenReturn(mockResponse);

        ResponseEntity<List<SensorDataDTO>> response = sensorDataController.createSensorData(input);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(4, response.getBody().size());
        verify(sensorDataService, times(1)).createSensorData(input);
    }


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
