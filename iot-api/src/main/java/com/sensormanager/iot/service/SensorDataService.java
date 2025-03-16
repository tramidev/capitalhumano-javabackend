package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.SensorDataAdapter;
import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.model.SensorData;
import com.sensormanager.iot.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public List<SensorDataDTO> getAllSensorData() {
        return sensorDataRepository.findAll().stream()
                .map(SensorDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    public SensorDataDTO getSensorDataById(Long id) {
        return sensorDataRepository.findById(id)
                .map(SensorDataAdapter::toDTO)
                .orElse(null);
    }

    public SensorDataDTO createSensorData(SensorDataDTO sensorDataDTO) {
        SensorData sensorData = SensorDataAdapter.toEntity(sensorDataDTO);
        SensorData savedSensorData = sensorDataRepository.save(sensorData);
        return SensorDataAdapter.toDTO(savedSensorData);
    }

    public SensorDataDTO updateSensorData(Long id, SensorDataDTO sensorDataDTO) {
        return sensorDataRepository.findById(id).map(existingSensor -> {
            existingSensor.setValue(sensorDataDTO.getValue());
            existingSensor.setTimestamp(sensorDataDTO.getTimestamp());
            SensorData updatedSensor = sensorDataRepository.save(existingSensor);
            return SensorDataAdapter.toDTO(updatedSensor);
        }).orElse(null);
    }

    public void deleteSensorData(Long id) {
        sensorDataRepository.deleteById(id);
    }

    // Nuevo método:
    public List<SensorDataDTO> getSensorDataBySensorIdsAndDateRange(List<Long> sensorIds, LocalDateTime from, LocalDateTime to) {
        return sensorDataRepository.findAll().stream()
            .filter(sd -> sensorIds.contains(sd.getSensorId()) &&
                          !sd.getTimestamp().isBefore(from) &&
                          !sd.getTimestamp().isAfter(to))
            .map(SensorDataAdapter::toDTO)
            .collect(Collectors.toList());
    }
}
