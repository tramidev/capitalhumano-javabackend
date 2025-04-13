package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationDTO>> findAll() {
        List<LocationDTO> locationDTOs = locationService.findAll();
        if (locationDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> findById(@PathVariable Long id) {
        LocationDTO locationDTO = locationService.findById(id);
        if (locationDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The location ID: " + id + " does not exist.");
        }
        return ResponseEntity.ok(locationDTO);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> create(@RequestBody LocationDTO locationDTO) {
        LocationDTO insertedLocationDTO = locationService.create(locationDTO);
        if (insertedLocationDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not inserted.");
        }
        return ResponseEntity.ok(insertedLocationDTO);
    }

    @PutMapping
    public ResponseEntity<LocationDTO> update(@RequestBody LocationDTO locationDTO) {
        LocationDTO updatedLocationDTO = locationService.update(locationDTO);
        if (updatedLocationDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not updated.");
        }
        return ResponseEntity.ok(updatedLocationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LocationDTO> deleteById(@PathVariable Long id) {
        LocationDTO deletedLocationDTO = locationService.deleteById(id);
        if (deletedLocationDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not disabled.");
        }
        return ResponseEntity.ok(deletedLocationDTO);
    }
}
