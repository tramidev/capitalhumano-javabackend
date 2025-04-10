package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import com.sensormanager.iot.service.SensorDataServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensordata")
public class SensorDataController {

	@Autowired
    private SensorDataServiceImp sensorDataService;

    @GetMapping
    public ResponseEntity<List<SensorDataDTO>> getSensorData(@RequestParam("sensor_id") List<Long> sensorIds,
    														 @RequestParam("from") Long fromEpoch,
												             @RequestParam("to") Long toEpoch) {
        List<SensorDataDTO> sensorDataList = sensorDataService.getSensorData(sensorIds, fromEpoch, toEpoch);
        if(sensorDataList.size() == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(sensorDataList);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(sensorDataList);
    }

    @PostMapping
    public ResponseEntity<List<SensorDataDTO>> createSensorData(@RequestBody SensorJSONPackageDTO sensorDataDTO) {
        List<SensorDataDTO> createdSensorData = sensorDataService.createSensorData(sensorDataDTO);
        if(createdSensorData == null || createdSensorData.size() == 0){
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The data was not inserted.");
        }
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(createdSensorData);
    }
    
}