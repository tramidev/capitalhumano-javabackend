package com.sensormanager.iot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
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
import com.sensormanager.iot.service.SensorDataServiceImp;

class SensorDataControllerTest {

    @Mock
    private SensorDataServiceImp sensorDataService;

    @InjectMocks
    private SensorDataController sensorDataController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void testGetAllSensorData() {
        List<SensorDataDTO> mockData = Arrays.asList(new SensorDataDTO(), new SensorDataDTO());
        when(sensorDataService.getAllSensorData()).thenReturn(mockData);
        List<Long> sensorIds = new ArrayList();
        sensorIds.add(Long.getLong("2"));
        ResponseEntity<List<SensorDataDTO>> response = sensorDataController.getSensorData(sensorIds, Long.getLong("1743642108"), Long.getLong("17436421999"));
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(mockData, response.getBody());
        verify(sensorDataService, times(1)).getAllSensorData();
    }

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
}