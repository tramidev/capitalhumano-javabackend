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
        List<LocationDTO> locationsDto = locationService.findAll();
        if (locationsDto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(locationsDto);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(locationsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> findById(@PathVariable Long id) {
        LocationDTO locationDto = locationService.findById(id);
        if (locationDto == null) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The location ID: " + id + " does not exist.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(locationDto);
    }


    @PostMapping
    public ResponseEntity<LocationDTO> create(@RequestBody LocationDTO locationDto) {
        LocationDTO locationInserted = locationService.create(locationDto);
        if (locationInserted.getId() == null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not inserted.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(locationInserted);
    }

    @PutMapping
    public ResponseEntity<LocationDTO> update(@RequestBody LocationDTO locationDto) {
        LocationDTO locationUpdated = locationService.update(locationDto);
        if (locationUpdated.getId() == null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not updated.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(locationUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LocationDTO> deleteById(@PathVariable Long id) {
        LocationDTO locationDeleted = locationService.deleteById(id);
        if (locationDeleted.getId() == null) {
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not disabled.");
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(locationDeleted);
    }
}
