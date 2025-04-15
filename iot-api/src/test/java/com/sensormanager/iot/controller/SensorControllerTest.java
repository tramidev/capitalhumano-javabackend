package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.service.SensorService;

class SensorControllerTest {

    @InjectMocks
    private SensorController sensorController;

    @Mock
    private SensorService sensorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_WhenSensorsExist() {
        List<SensorDTO> sensors = Arrays.asList(
                new SensorDTO(1L, "Sensor1", "Chile", 1L, 2L, 1742854426L, true),
                new SensorDTO(2L, "Sensor2", "Chile", 1L, 2L, 1742854426L, true)
        );
        when(sensorService.findAll()).thenReturn(sensors);

        ResponseEntity<List<SensorDTO>> response = sensorController.findAll();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensors, response.getBody());
        verify(sensorService, times(1)).findAll();
    }

    @Test
    void testFindAll_WhenNoSensorsExist() {
        when(sensorService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<SensorDTO>> response = sensorController.findAll();

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
        assertNull(response.getBody());
        verify(sensorService, times(1)).findAll();
    }

    @Test
    void testFindById_WhenSensorExists() {
        SensorDTO sensor = new SensorDTO(1L, "Sensor1", "Chile", 1L, 2L, 1742854426L, true);
        when(sensorService.findById(1L)).thenReturn(sensor);

        ResponseEntity<SensorDTO> response = sensorController.findById(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).findById(1L);
    }

    @Test
    void testFindById_WhenSensorDoesNotExist() {
        when(sensorService.findById(1L)).thenReturn(new SensorDTO());

        assertThrows(ResponseStatusException.class, () -> sensorController.findById(1L));
        verify(sensorService, times(1)).findById(1L);
    }

    @Test
    void testFindById_WhenAccessIsDenied() {
        Long sensorId = 1L;

        when(sensorService.findById(sensorId))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to sensor."));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sensorController.findById(sensorId)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Access denied to sensor.", exception.getReason());
        verify(sensorService, times(1)).findById(sensorId);
    }


    @Test
    void testCreate_WhenSensorIsValid() {
        SensorDTO sensor = new SensorDTO(1L, "Sensor1", "Chile", 1L, 2L, 1742854426L, true);
        when(sensorService.create(sensor)).thenReturn(sensor);

        ResponseEntity<SensorDTO> response = sensorController.create(sensor);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).create(sensor);
    }

    @Test
    void testCreate_WhenSensorIsInvalid() {
        SensorDTO sensor = new SensorDTO(null, "Sensor1", "Chile", 1L, 2L, 1742854426L, true);
        when(sensorService.create(sensor)).thenReturn(new SensorDTO());

        assertThrows(ResponseStatusException.class, () -> sensorController.create(sensor));
        verify(sensorService, times(1)).create(sensor);
    }

    @Test
    void testCreate_WhenSensorNameIsMissing() {
        SensorDTO invalidSensor = new SensorDTO();
        invalidSensor.setSensorName(null); // o vacío

        when(sensorService.create(invalidSensor))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sensor name is required."));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sensorController.create(invalidSensor)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Sensor name is required.", exception.getReason());
        verify(sensorService, times(1)).create(invalidSensor);
    }

    @Test
    void testCreate_WhenNoAuthenticatedCompany() {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setSensorName("SensorX");

        when(sensorService.create(sensorDTO))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated company found."));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> sensorController.create(sensorDTO)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("No authenticated company found.", exception.getReason());
        verify(sensorService, times(1)).create(sensorDTO);
    }



    @Test
    void testUpdate_WhenSensorIsValid() {
        SensorDTO sensor = new SensorDTO(1L, "UpdatedSensor", "Chile", 1L, 2L, 1742854426L, true);
        when(sensorService.update(sensor)).thenReturn(sensor);

        ResponseEntity<SensorDTO> response = sensorController.update(sensor);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).update(sensor);
    }

    @Test
    void testUpdate_WhenSensorIsInvalid() {
        SensorDTO sensor = new SensorDTO(1L, "Invalid", "Chile", 1L, 2L, 1742854426L, true);
        when(sensorService.update(sensor)).thenReturn(new SensorDTO());

        assertThrows(ResponseStatusException.class, () -> sensorController.update(sensor));
        verify(sensorService, times(1)).update(sensor);
    }

    @Test
    void testDeleteById_WhenSensorExists() {
        SensorDTO sensor = new SensorDTO(1L, "Sensor1", "Chile", 1L, 2L, 1742854426L, true);
        when(sensorService.deleteById(1L)).thenReturn(sensor);

        ResponseEntity<SensorDTO> response = sensorController.deleteById(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_WhenSensorDoesNotExist() {
        when(sensorService.deleteById(1L)).thenReturn(new SensorDTO());

        assertThrows(ResponseStatusException.class, () -> sensorController.deleteById(1L));
        verify(sensorService, times(1)).deleteById(1L);
    }
}
