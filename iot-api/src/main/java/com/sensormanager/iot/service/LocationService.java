package com.sensormanager.iot.service;

import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.model.Location;

import java.util.List;

public interface LocationService {

    List<LocationDTO> findAll();
    LocationDTO findById(Long id);
    LocationDTO create(LocationDTO locationDTO);
    LocationDTO update(LocationDTO locationDTO);
    LocationDTO deleteById(Long id);
}
