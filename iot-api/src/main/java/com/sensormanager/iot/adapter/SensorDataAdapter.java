package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Location;
import com.sensormanager.iot.model.Sensor;

public class SensorDataAdapter {

    public static SensorDTO toDTO(Sensor sensor) {
        return SensorDTO.builder()
                .id(sensor.getId())
                .sensorName(sensor.getSensorName())
                .sensorApiKey(sensor.getSensorApiKey())
                .sensorLocation(sensor.getSensorLocation() != null ? sensor.getSensorLocation().getId() : null)
                .sensorCompany(sensor.getSensorCompany() != null ? sensor.getSensorCompany().getId() : null)
                .sensorCreatedAt(sensor.getSensorCreatedAt())
                .sensorStatus(sensor.getSensorStatus())
                .build();
    }

    public static Sensor toEntity(SensorDTO dto) {
        return Sensor.builder()
                .id(dto.getId())
                .sensorName(dto.getSensorName())
                .sensorApiKey(dto.getSensorApiKey())
                .sensorLocation(dto.getSensorLocation() != null ? Location.builder().id(dto.getSensorLocation()).build() : null)
                .sensorCompany(dto.getSensorCompany() != null ? Company.builder().id(dto.getSensorCompany()).build() : null)
                .sensorCreatedAt(dto.getSensorCreatedAt())
                .sensorStatus(dto.getSensorStatus())
                .build();
    }
}
