package com.sensormanager.iot.service;

import com.sensormanager.iot.dto.SensorDTO;

import java.util.List;


public interface SensorService {

	public List<SensorDTO> findAll();

    public SensorDTO findById(Long id);

    public List<SensorDTO> findByCompany(Long id);

    public SensorDTO create(SensorDTO sensorDTO);

    public SensorDTO update(SensorDTO sensorDTO);

    public SensorDTO deleteById(Long id);
}
