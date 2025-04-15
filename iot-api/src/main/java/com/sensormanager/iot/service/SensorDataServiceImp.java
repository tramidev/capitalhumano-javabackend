package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.SensorDataDataAdapter;
import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.model.SensorData;
import com.sensormanager.iot.repository.SensorDataRepository;
import com.sensormanager.iot.repository.SensorRepository;
import com.sensormanager.iot.security.AuthenticatedService;
import com.sensormanager.iot.security.CustomUserSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImp implements SensorDataService {

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private AuthenticatedService authenticatedService;

    public List<SensorDataDTO> getAllSensorData() {
        return sensorDataRepository.findAll().stream()
                .map(SensorDataDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    public SensorDataDTO getSensorDataById(Long id) {
        return sensorDataRepository.findById(id)
                .map(SensorDataDataAdapter::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor data not found."));
    }

    @Transactional
    public SensorDataDTO createSensorData(SensorDataDTO sensorDataDTO) {
        if (sensorDataDTO.getIdSensor() == null || sensorDataDTO.getMetric() == null || sensorDataDTO.getRecord() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sensor data is incomplete.");
        }

        SensorData sensorData = SensorDataDataAdapter.toEntity(sensorDataDTO);
        SensorData savedSensorData = sensorDataRepository.save(sensorData);
        return SensorDataDataAdapter.toDTO(savedSensorData);
    }

    @Transactional
    public List<SensorDataDTO> createSensorData(SensorJSONPackageDTO sensorJSONPackageDTO) {
        if (sensorJSONPackageDTO == null || sensorJSONPackageDTO.getApiKey() == null || sensorJSONPackageDTO.getJsonData() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sensor data package.");
        }

        Optional<Sensor> sensor = sensorRepository.findSensorBySensorApiKey(sensorJSONPackageDTO.getApiKey());
        if (sensor.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor with given API key not found.");
        }

        Long sensorID = sensor.get().getId();
        List<SensorDataDTO> sensorDatas = new ArrayList<>();

        for (Map<String, String> obj : sensorJSONPackageDTO.getJsonData()) {
            String datetimeStr = obj.remove("datetime");
            if (datetimeStr == null) continue;

            try {
                Long createdAt = Long.parseLong(datetimeStr);
                for (Map.Entry<String, String> entry : obj.entrySet()) {
                    SensorDataDTO sensorDataDTO = new SensorDataDTO();
                    sensorDataDTO.setIdSensor(sensorID);
                    sensorDataDTO.setMetric(entry.getKey());
                    sensorDataDTO.setRecord(entry.getValue());
                    sensorDataDTO.setRecordCreatedAt(createdAt);
                    sensorDatas.add(sensorDataDTO);
                }
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid datetime format in sensor data.");
            }
        }

        if (sensorDatas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid sensor data found.");
        }

        return sensorDatas.stream()
                .map(this::createSensorData)
                .collect(Collectors.toList());
    }

    @Transactional
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
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor data to update not found."));
    }


    @Transactional
    public void deleteSensorData(Long id) {
        if (!sensorDataRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor data to delete not found.");
        }
        sensorDataRepository.deleteById(id);
    }

    public List<SensorDataDTO> getSensorData(List<Long> sensorIds, Long from, Long to) {
        CustomUserSecurity userAuth = authenticatedService.getAuthenticatedUser();

        if (authenticatedService.isRootUser()) {
            return sensorDataRepository.findBySensorIdInAndRecordCreatedAtBetween(sensorIds, from, to)
                    .stream()
                    .map(SensorDataDataAdapter::toDTO)
                    .collect(Collectors.toList());
        } else {
            Long companyId = authenticatedService.getUserCompanyId();

            List<Long> validSensorIds = sensorRepository
                    .findByIdInAndSensorCompanyId(sensorIds, companyId)
                    .stream()
                    .map(Sensor::getId)
                    .toList();

            if (validSensorIds.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access these sensors.");
            }

            return sensorDataRepository.findBySensorIdInAndRecordCreatedAtBetween(validSensorIds, from, to)
                    .stream()
                    .map(SensorDataDataAdapter::toDTO)
                    .collect(Collectors.toList());
        }
    }
}
