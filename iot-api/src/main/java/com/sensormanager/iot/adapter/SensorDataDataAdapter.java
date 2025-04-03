package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.model.SensorData;

public class SensorDataDataAdapter {

    // Convierte una entidad SensorData a un DTO
    public static SensorDataDTO toDTO(SensorData sensorData) {
        return SensorDataDTO.builder()
                .id(sensorData.getId())
                .idSensor(sensorData.getSensor().getId())
                .recordCreatedAt(sensorData.getRecordCreatedAt())
                .record(sensorData.getRecord())
                .metric(sensorData.getMetric())
                .build();
    }

    // Convierte un DTO a una entidad SensorData
    public static SensorData toEntity(SensorDataDTO dto) {
        return SensorData.builder()
                .id(dto.getId())
                .sensor(Sensor.builder().id(dto.getIdSensor()).build())
                .record(dto.getRecord())
                .recordCreatedAt(dto.getRecordCreatedAt())
                .metric(dto.getMetric())
                .build();
    }
}
