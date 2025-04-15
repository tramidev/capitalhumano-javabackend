package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.service.SensorDataServiceImp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorDataControllerTest {

    @Mock
    private SensorDataServiceImp sensorDataService;

    @InjectMocks
    private SensorDataController sensorDataController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSensorData() {
        List<Long> sensorIds = List.of(1L, 2L);
        Long fromEpoch = 1742861430L;
        Long toEpoch = 1742861495L;

        List<SensorDataDTO> mockData = List.of(
                SensorDataDTO.builder().id(1L).idSensor(1L).metric("temp").record("24.4").recordCreatedAt(1742861430L).build()
        );

        when(sensorDataService.getSensorData(sensorIds, fromEpoch, toEpoch)).thenReturn(mockData);

        ResponseEntity<List<SensorDataDTO>> response = sensorDataController.getSensorData(sensorIds, fromEpoch, toEpoch);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockData, response.getBody());
        verify(sensorDataService, times(1)).getSensorData(sensorIds, fromEpoch, toEpoch);
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

        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertEquals(4, response.getBody().size());
        verify(sensorDataService, times(1)).createSensorData(input);
    }

    @Test
    void testCreateSensorData_ReturnsBadRequest() {
        SensorJSONPackageDTO invalidInput = SensorJSONPackageDTO.builder()
                .apiKey("invalid-api-key")
                .jsonData(List.of()) // sin datos válidos
                .build();

        when(sensorDataService.createSensorData(invalidInput))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid sensor data found."));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sensorDataController.createSensorData(invalidInput)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("No valid sensor data found.", exception.getReason());
    }

    @Test
    void testGetSensorData_ReturnsForbidden() {
        List<Long> sensorIds = List.of(1L);
        Long fromEpoch = 1742861430L;
        Long toEpoch = 1742861495L;

        when(sensorDataService.getSensorData(sensorIds, fromEpoch, toEpoch))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied."));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sensorDataController.getSensorData(sensorIds, fromEpoch, toEpoch)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Access denied.", exception.getReason());
    }

    @Test
    void testGetSensorData_ReturnsUnauthorized() {
        List<Long> sensorIds = List.of(1L);
        Long fromEpoch = 1742861430L;
        Long toEpoch = 1742861495L;

        when(sensorDataService.getSensorData(sensorIds, fromEpoch, toEpoch))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authenticated."));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sensorDataController.getSensorData(sensorIds, fromEpoch, toEpoch)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("You are not authenticated.", exception.getReason());
    }


}

