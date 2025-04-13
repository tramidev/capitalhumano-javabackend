package com.sensormanager.iot.service;

import java.util.List;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.dto.SensorJSONPackageDTO;

public interface SersorDataService {
	
	public List<SensorDataDTO> createSensorData(SensorJSONPackageDTO sensorJSONPackageDTO);
	public List<SensorDataDTO> getSensorData(List<Long> sensorIds, Long from, Long to);
	
}
