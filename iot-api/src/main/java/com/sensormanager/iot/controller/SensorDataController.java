package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

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

    // Endpoint de inserción masiva según la actividad
    @PostMapping("/bulk")
    public ResponseEntity<Void> insertSensorData(@RequestBody Map<String, Object> payload) {

        String apiKey = (String) payload.get("api_key");
        if(apiKey == null || !sensorDataService.validateSensorApiKey(apiKey)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        sensorDataService.saveBulkSensorData(apiKey, payload);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Endpoint de consulta avanzada según actividad
    @GetMapping("/query")
    public ResponseEntity<List<SensorDataDTO>> querySensorData(
        @RequestHeader(value = "Authorization", required = false) String headerApiKey,
        @RequestParam(value = "company_api_key", required = false) String paramApiKey,
        @RequestParam("from") Long fromEpoch,
        @RequestParam("to") Long toEpoch,
        @RequestParam("sensor_id") List<Long> sensorIds) {

        String apiKey = headerApiKey != null ? headerApiKey : paramApiKey;

        if (apiKey == null || !apiKey.equals("TU_API_KEY_SEGURA")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        LocalDateTime from = LocalDateTime.ofEpochSecond(fromEpoch, 0, ZoneOffset.UTC);
        LocalDateTime to = LocalDateTime.ofEpochSecond(toEpoch, 0, ZoneOffset.UTC);

        List<SensorDataDTO> data = sensorDataService.getSensorDataBySensorIdsAndDateRange(sensorIds, from, to);
        return ResponseEntity.ok(data);
    }
}
