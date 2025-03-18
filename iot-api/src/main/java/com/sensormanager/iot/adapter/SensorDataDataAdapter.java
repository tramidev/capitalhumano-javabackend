package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.model.SensorData;

public class SensorDataDataAdapter {

    // Convierte una entidad SensorData a un DTO
    public static SensorDataDTO toDTO(SensorData sensorData) {
        return new SensorDataDTO(
            sensorData.getId(),
            String.valueOf(sensorData.getSensorId()), // Integer a String
            sensorData.getValue(),
            sensorData.getTimestamp()
        );
    }

    // Convierte un DTO a una entidad SensorData
    public static SensorData toEntity(SensorDataDTO sensorDataDTO) {
        Long sensorId = (sensorDataDTO.getSensorId() != null) ? Long.parseLong(sensorDataDTO.getSensorId()) : null;
        
        return new SensorData(
            sensorId,  // ✅ Ahora manejamos el caso de null
            sensorDataDTO.getValue(),
            sensorDataDTO.getTimestamp()
        );
    }
}
