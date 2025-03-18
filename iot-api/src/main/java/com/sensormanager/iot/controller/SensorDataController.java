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
@RequestMapping("/api/v1/sensor_data")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    // Obtener todos los datos de sensores
    @GetMapping
    public ResponseEntity<List<SensorDataDTO>> getAllSensorData(
            @RequestHeader(value = "Authorization", required = false) String headerApiKey,
            @RequestParam(value = "company_api_key", required = false) String paramApiKey) {

        String apiKey = headerApiKey != null ? headerApiKey : paramApiKey;

        if (apiKey == null || !apiKey.equals("TU_API_KEY_SEGURA")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<SensorDataDTO> sensorDataList = sensorDataService.getAllSensorData();
        return ResponseEntity.ok(sensorDataList);
    }

    // Obtener datos de un sensor por ID
    @GetMapping("/{id}")
    public ResponseEntity<SensorDataDTO> getSensorDataById(
            @RequestHeader(value = "Authorization", required = false) String headerApiKey,
            @RequestParam(value = "company_api_key", required = false) String paramApiKey,
            @PathVariable Long id) {

        String apiKey = headerApiKey != null ? headerApiKey : paramApiKey;

        if (apiKey == null || !apiKey.equals("TU_API_KEY_SEGURA")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SensorDataDTO sensorDataDTO = sensorDataService.getSensorDataById(id);
        return (sensorDataDTO != null) ? ResponseEntity.ok(sensorDataDTO) : ResponseEntity.notFound().build();
    }

    // Crear un nuevo registro de sensor (individual)
    @PostMapping
    public ResponseEntity<SensorDataDTO> createSensorData(@RequestBody SensorDataDTO sensorDataDTO) {
        SensorDataDTO createdSensorData = sensorDataService.createSensorData(sensorDataDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSensorData);
    }

    // Actualizar datos de un sensor
    @PutMapping("/{id}")
    public ResponseEntity<SensorDataDTO> updateSensorData(
            @RequestHeader(value = "Authorization", required = false) String headerApiKey,
            @RequestParam(value = "company_api_key", required = false) String paramApiKey,
            @PathVariable Long id, 
            @RequestBody SensorDataDTO sensorDataDTO) {

        String apiKey = headerApiKey != null ? headerApiKey : paramApiKey;

        if (apiKey == null || !apiKey.equals("TU_API_KEY_SEGURA")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SensorDataDTO updatedSensorData = sensorDataService.updateSensorData(id, sensorDataDTO);
        return (updatedSensorData != null) ? ResponseEntity.ok(updatedSensorData) : ResponseEntity.notFound().build();
    }

    // Eliminar un registro de sensor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorData(
            @RequestHeader(value = "Authorization", required = false) String headerApiKey,
            @RequestParam(value = "company_api_key", required = false) String paramApiKey,
            @PathVariable Long id) {

        String apiKey = headerApiKey != null ? headerApiKey : paramApiKey;

        if (apiKey == null || !apiKey.equals("TU_API_KEY_SEGURA")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        sensorDataService.deleteSensorData(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint de inserción masiva usando sensor_api_key según actividad
    @PostMapping("/bulk")
    public ResponseEntity<Void> insertSensorData(@RequestBody Map<String, Object> payload) {

        String apiKey = (String) payload.get("api_key");
        if(apiKey == null || !apiKey.equals("TU_SENSOR_API_KEY")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<Map<String, Object>> jsonData = (List<Map<String, Object>>) payload.get("json_data");
        sensorDataService.saveBulkSensorData(apiKey, jsonData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Consulta avanzada de sensor_data según actividad
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
