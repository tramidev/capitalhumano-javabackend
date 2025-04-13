package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.SensorDataDataAdapter;
import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.model.SensorData;
import com.sensormanager.iot.repository.SensorDataRepository;
import com.sensormanager.iot.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorRepository sensorRepository;

    public List<SensorDataDTO> getAllSensorData() {
        return sensorDataRepository.findAll().stream()
                .map(SensorDataDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    public SensorDataDTO getSensorDataById(Long id) {
        return sensorDataRepository.findById(id)
                .map(SensorDataDataAdapter::toDTO)
                .orElse(null);
    }

    public SensorDataDTO createSensorData(SensorDataDTO sensorDataDTO) {
        SensorData sensorData = SensorDataDataAdapter.toEntity(sensorDataDTO);
        SensorData savedSensorData = sensorDataRepository.save(sensorData);
        return SensorDataDataAdapter.toDTO(savedSensorData);
    }

    public List<SensorDataDTO> createSensorData(SensorJSONPackageDTO sensorJSONPackageDTO) {
        List<SensorDataDTO> sensorDatas = new ArrayList<>();

        Optional<Sensor> sensor = sensorRepository.findSensorBySensorApiKey(sensorJSONPackageDTO.getApiKey());
        if (sensor.isEmpty()) return null;

        Long sensorID = sensor.get().getId();

        for (Map<String, String> obj : sensorJSONPackageDTO.getJsonData()) {
            Long createdAt = Long.parseLong(obj.get("datetime"));
            obj.remove("datetime");


            for (Map.Entry<String, String> entry : obj.entrySet()) {
                SensorDataDTO sensorDataDTO = new SensorDataDTO();
                sensorDataDTO.setIdSensor(sensorID);
                sensorDataDTO.setMetric(entry.getKey());
                sensorDataDTO.setRecord(entry.getValue());
                sensorDataDTO.setRecordCreatedAt(createdAt);
                sensorDatas.add(sensorDataDTO);
            }
        }

        return sensorDatas.stream()
                .map(this::createSensorData)
                .collect(Collectors.toList());
    }

    public SensorDataDTO updateSensorData(Long id, SensorDataDTO sensorDataDTO) {
        return sensorDataRepository.findById(id).map(existingSensorData -> {

            if (sensorDataDTO.getIdSensor() != null) {
                Optional<Sensor> sensorOpt = sensorRepository.findById(sensorDataDTO.getIdSensor());
                sensorOpt.ifPresent(existingSensorData::setSensor);
            }

            if (sensorDataDTO.getMetric() != null) {
                existingSensorData.setMetric(sensorDataDTO.getMetric());
            }

            if (sensorDataDTO.getRecord() != null) {
                existingSensorData.setRecord(sensorDataDTO.getRecord());
            }

            if (sensorDataDTO.getRecordCreatedAt() != null) {
                existingSensorData.setRecordCreatedAt(sensorDataDTO.getRecordCreatedAt());
            }

            SensorData updatedEntity = sensorDataRepository.save(existingSensorData);
            return SensorDataDataAdapter.toDTO(updatedEntity);
        }).orElse(null);
    }


    public void deleteSensorData(Long id) {
        sensorDataRepository.deleteById(id);
    }
}
