package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.model.Sensor;

public class SensorDataAdapter {

    public static SensorDTO toDTO(Sensor Sensor) {
        return new SensorDTO(
                Sensor.getId(),
                Sensor.getSensorName(),
                Sensor.getSensorApiKey(),
                Sensor.getSensorLocation(),
                Sensor.getSensorCompany(),
                Sensor.getSensorCreatedAt(),
                Sensor.getSensorStatus()
        );
    }
    
    public static Sensor toEntity(SensorDTO sensorDTO) {
    	
        return new Sensor(
                sensorDTO.getId(),
                sensorDTO.getSensorName(),
                sensorDTO.getSensorApiKey(),
                sensorDTO.getSensorLocation(),
                sensorDTO.getSensorCompany(),
                sensorDTO.getSensorCreatedAt(),
                sensorDTO.getSensorStatus()
        );
    }
}
