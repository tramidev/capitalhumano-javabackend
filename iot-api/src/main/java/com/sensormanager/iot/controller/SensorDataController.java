package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensordata")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    // Obtener todos los datos de sensores
    @GetMapping
    public ResponseEntity<List<SensorDataDTO>> getAllSensorData() {
        List<SensorDataDTO> sensorDataList = sensorDataService.getAllSensorData();
        return ResponseEntity.ok(sensorDataList);
    }

    // Obtener datos de un sensor por ID
    @GetMapping("/{id}")
    public ResponseEntity<SensorDataDTO> getSensorDataById(@PathVariable Long id) {
        SensorDataDTO sensorDataDTO = sensorDataService.getSensorDataById(id);
        return (sensorDataDTO != null) ? ResponseEntity.ok(sensorDataDTO) : ResponseEntity.notFound().build();
    }

    // Crear un nuevo registro de sensor
    @PostMapping
    public ResponseEntity<SensorDataDTO> createSensorData(@RequestBody SensorDataDTO sensorDataDTO) {
        SensorDataDTO createdSensorData = sensorDataService.createSensorData(sensorDataDTO);
        return ResponseEntity.ok(createdSensorData);
    }

    // Actualizar datos de un sensor
    @PutMapping("/{id}")
    public ResponseEntity<SensorDataDTO> updateSensorData(@PathVariable Long id, @RequestBody SensorDataDTO sensorDataDTO) {
        SensorDataDTO updatedSensorData = sensorDataService.updateSensorData(id, sensorDataDTO);
        return (updatedSensorData != null) ? ResponseEntity.ok(updatedSensorData) : ResponseEntity.notFound().build();
    }

    // Eliminar un registro de sensor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorData(@PathVariable Long id) {
        sensorDataService.deleteSensorData(id);
        return ResponseEntity.noContent().build();
    }
}
