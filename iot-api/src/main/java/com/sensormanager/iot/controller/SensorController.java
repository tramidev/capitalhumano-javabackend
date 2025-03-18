package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {

	@Autowired
    private SensorService SensorService;
	
	@GetMapping
	public ResponseEntity<List<SensorDTO>> findAll(){
		List<SensorDTO> SensorsDto = SensorService.findAll();
		if(SensorsDto.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(SensorsDto);
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(SensorsDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<SensorDTO> findById(@PathVariable Long id){
		SensorDTO SensorsDto = SensorService.findById(id);
		if(SensorsDto.getId() == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(SensorsDto);
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(SensorsDto);
	}

	@GetMapping("/company/{id}")
	public ResponseEntity<List<SensorDTO>> findByCompanyId(@PathVariable Long id){
		List<SensorDTO> SensorsDto = SensorService.findByCompany(id);
		if(SensorsDto.isEmpty()){
			return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(SensorsDto);
		}
		return ResponseEntity.status(HttpStatus.OK.value()).body(SensorsDto);
	}

	    @PostMapping
	    public ResponseEntity<SensorDTO> create(@RequestBody SensorDTO sensorDTO){
			SensorDTO insertedSensor = SensorService.create(sensorDTO);
	        if(insertedSensor.getId() == null){
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(insertedSensor);
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(insertedSensor);
	    }

	    @PutMapping
	    public ResponseEntity<SensorDTO> update(@RequestBody SensorDTO sensorDTO){
			SensorDTO updatedSensor = SensorService.update(sensorDTO);
	        if(updatedSensor.getId() == null){
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(updatedSensor);
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(updatedSensor);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<SensorDTO> deleteById(@PathVariable Long id){
			SensorDTO deletedSensor = SensorService.deleteById(id);
	        if(deletedSensor.getId() == null){
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(deletedSensor);
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(deletedSensor);
	    }
}
