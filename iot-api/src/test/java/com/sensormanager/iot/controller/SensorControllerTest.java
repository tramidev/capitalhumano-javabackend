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
            new SensorDTO(1L, "Sensor1","Chile",1L,2L,1742854426L,true), 
            new SensorDTO(2L, "Sensor2","Chile",1L,2L,1742854426L,true));
        when(sensorService.findAll()).thenReturn(sensors);
        ResponseEntity<List<SensorDTO>> response = sensorController.findAll();
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
        assertEquals(sensors, response.getBody());
        verify(sensorService, times(1)).findAll();
    }

    @Test
    void testFindAll_WhenNoSensorsExist() {
        when(sensorService.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<SensorDTO>> response = sensorController.findAll();
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(sensorService, times(1)).findAll();
    }

    @Test
    void testFindById_WhenSensorExists() {
        SensorDTO sensor = new SensorDTO(1L, "Sensor1","Chile",1L,2L,1742854426L,true);
        when(sensorService.findById(1L)).thenReturn(sensor);
        ResponseEntity<SensorDTO> response = sensorController.findById(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).findById(1L);
    }

    @Test
    void testFindById_WhenSensorDoesNotExist() {
        SensorDTO sensor = new SensorDTO(null, null,null,null,null,null,null);
        when(sensorService.findById(1L)).thenReturn(sensor);
        ResponseEntity<SensorDTO> response = sensorController.findById(1L);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).findById(1L);
    }

    @Test
    void testCreate_WhenSensorIsValid() {
        SensorDTO sensor = new SensorDTO(1L, "Sensor1","Chile",1L,2L,1742854426L,true);
        SensorDTO createdSensor = new SensorDTO(1L,  "Sensor1","Chile",1L,2L,1742854426L,true);
        when(sensorService.create(sensor)).thenReturn(createdSensor);
        ResponseEntity<SensorDTO> response = sensorController.create(sensor);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(createdSensor, response.getBody());
        verify(sensorService, times(1)).create(sensor);
    }

    @Test
    void testCreate_WhenSensorIsInvalid() {
        SensorDTO sensor = new SensorDTO(null, "Sensor1" ,"Chile",1L,2L,1742854426L,true);
        SensorDTO createdSensor = new SensorDTO(null,  "Sensor1","Chile",1L,2L,1742854426L,true);
        when(sensorService.create(sensor)).thenReturn(createdSensor);
        ResponseEntity<SensorDTO> response = sensorController.create(sensor);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals(createdSensor, response.getBody());
        verify(sensorService, times(1)).create(sensor);
    }

    @Test
    void testUpdate_WhenSensorIsValid() {
        SensorDTO sensor = new SensorDTO(1L, "UpdatedSensor","Chile",1L,2L,1742854426L,true);
        when(sensorService.update(sensor)).thenReturn(sensor);
        ResponseEntity<SensorDTO> response = sensorController.update(sensor);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).update(sensor);
    }

    @Test
    void testUpdate_WhenSensorIsInvalid() {
        SensorDTO sensor = new SensorDTO(1L, "UpdatedSensor", "Chile",1L,2L,1742854426L,true);
        SensorDTO updatedSensor = new SensorDTO(null, "Sensor1","Chile",1L,2L,1742854426L,true);
        when(sensorService.update(sensor)).thenReturn(updatedSensor);
        ResponseEntity<SensorDTO> response = sensorController.update(sensor);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals(updatedSensor, response.getBody());
        verify(sensorService, times(1)).update(sensor);
    }

    @Test
    void testDeleteById_WhenSensorExists() {
        SensorDTO sensor = new SensorDTO(1L, "Sensor1","Chile",1L,2L,1742854426L,true);
        when(sensorService.deleteById(1L)).thenReturn(sensor);
        ResponseEntity<SensorDTO> response = sensorController.deleteById(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_WhenSensorDoesNotExist() {
        SensorDTO sensor = new SensorDTO(null,  null,null,null,null,null,null);
        when(sensorService.deleteById(1L)).thenReturn(sensor);
        ResponseEntity<SensorDTO> response = sensorController.deleteById(1L);
        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        assertEquals(sensor, response.getBody());
        verify(sensorService, times(1)).deleteById(1L);
    }
}