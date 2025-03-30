package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {

	@Autowired
    private SensorService sensorService;
	
	@GetMapping
	public ResponseEntity<List<SensorDTO>> findAll(){
		List<SensorDTO> SensorsDto = sensorService.findAll();
		if(SensorsDto.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(SensorsDto);
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(SensorsDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SensorDTO> findById(@PathVariable Long id) {
		SensorDTO sensorDto = sensorService.findById(id);
		if (sensorDto == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The sensor ID: " + id + " does not exist.");
		}
		return ResponseEntity.ok(sensorDto);
	}



	@GetMapping("/company/{id}")
	public ResponseEntity<List<SensorDTO>> findByCompanyId(@PathVariable Long id){
		List<SensorDTO> SensorsDto = sensorService.findByCompany(id);
		if(SensorsDto.isEmpty()){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company ID: " + id + "has no sensors.");
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(SensorsDto);
	}

    @PostMapping
    public ResponseEntity<SensorDTO> create(@RequestBody SensorDTO sensorDTO){
		SensorDTO insertedSensor = sensorService.create(sensorDTO);
        if(insertedSensor.getId() == null){
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sensor was not inserted.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(insertedSensor);
    }

    @PutMapping
    public ResponseEntity<SensorDTO> update(@RequestBody SensorDTO sensorDTO){
		SensorDTO updatedSensor = sensorService.update(sensorDTO);
        if(updatedSensor.getId() == null){
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sensor was not updated.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(updatedSensor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SensorDTO> deleteById(@PathVariable Long id){
		SensorDTO deletedSensor = sensorService.deleteById(id);
        if(deletedSensor.getId() == null){
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sensor was not disabled.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(deletedSensor);
    }
    
}
