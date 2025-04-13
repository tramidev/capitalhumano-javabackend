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
	public ResponseEntity<List<SensorDTO>> findAll() {
		List<SensorDTO> sensorsDto = sensorService.findAll();
		if (sensorsDto.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(sensorsDto);
	}


	@GetMapping("/{id}")
	public ResponseEntity<SensorDTO> findById(@PathVariable Long id) {
		SensorDTO sensorDTO = sensorService.findById(id);
		if (sensorDTO.getId() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The sensor ID: " + id + " does not exist.");
		}
		return ResponseEntity.ok(sensorDTO);
	}

	@GetMapping("/company/{id}")
	public ResponseEntity<List<SensorDTO>> findByCompanyId(@PathVariable Long id) {
		List<SensorDTO> sensorDTOs = sensorService.findByCompany(id);
		if (sensorDTOs.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company ID: " + id + " has no sensors.");
		}
		return ResponseEntity.ok(sensorDTOs);
	}

	@PostMapping
	public ResponseEntity<SensorDTO> create(@RequestBody SensorDTO sensorDTO) {
		SensorDTO createdSensor = sensorService.create(sensorDTO);
		if (createdSensor.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sensor was not inserted.");
		}
		return ResponseEntity.ok(createdSensor);
	}

	@PutMapping
	public ResponseEntity<SensorDTO> update(@RequestBody SensorDTO sensorDTO) {
		SensorDTO updatedSensor = sensorService.update(sensorDTO);
		if (updatedSensor.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sensor was not updated.");
		}
		return ResponseEntity.ok(updatedSensor);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<SensorDTO> deleteById(@PathVariable Long id) {
		SensorDTO deletedSensor = sensorService.deleteById(id);
		if (deletedSensor.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sensor was not disabled.");
		}
		return ResponseEntity.ok(deletedSensor);
	}
}
