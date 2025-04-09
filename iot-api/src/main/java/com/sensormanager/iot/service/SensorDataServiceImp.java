package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.SensorDataDataAdapter;
import com.sensormanager.iot.dto.SensorJSONPackageDTO;
import com.sensormanager.iot.dto.SensorDataDTO;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.repository.SensorRepository;
import com.sensormanager.iot.security.CustomUserSecurity;
import com.sensormanager.iot.model.SensorData;
import com.sensormanager.iot.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImp implements SersorDataService{

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorRepository sensorRepository;
        
    public SensorDataDTO createSensorData(SensorDataDTO sensorDataDTO) {
        SensorData sensorData = SensorDataDataAdapter.toEntity(sensorDataDTO);
        SensorData savedSensorData = sensorDataRepository.save(sensorData);
        return SensorDataDataAdapter.toDTO(savedSensorData);
    }

    public List<SensorDataDTO> createSensorData(SensorJSONPackageDTO sensorJSONPackageDTO) {
        List<SensorDataDTO> sensorDatas = new ArrayList<>();

        Optional<Sensor> sensor = sensorRepository.findSensorBySensorApiKey(sensorJSONPackageDTO.api_key);
        if (sensor.isEmpty()) return null;

        Long sensorID = sensor.get().getId();
        
        for (Map<String, String> obj : sensorJSONPackageDTO.json_data) {
            Long createdAt = Long.parseLong(obj.get("datetime"));
            obj.remove(("datetime"));
            for (Map.Entry<String, String> entry : obj.entrySet()) {
                SensorDataDTO sensorDataDTO = new SensorDataDTO();
                sensorDataDTO.setIdSensor(sensorID);
                sensorDataDTO.setMetric(entry.getKey());
                sensorDataDTO.setRecord(entry.getValue());
                sensorDataDTO.setRecordCreatedAt(createdAt);
                sensorDatas.add(sensorDataDTO);
            }
        }
        return sensorDatas.stream().map(this::createSensorData).collect(Collectors.toList());
    }
    
    public List<SensorDataDTO> getSensorData(List<Long> sensorIds, Long from, Long to) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (userAuth.hasRole("ROOT")) {
        		return sensorDataRepository.findBySensorIdInAndRecordCreatedAtBetween(sensorIds, from, to)
    	                .stream()
    	                .map(SensorDataDataAdapter::toDTO)
    	                .collect(Collectors.toList());
        	} else {        		
        		List<Long> validSensorsIds = sensorRepository.findByIdInAndSensorCompanyId(sensorIds, userAuth.getCompany().getId()).stream().map(Sensor::getId).toList();        	    
        	    if (validSensorsIds.isEmpty()) {
        	        return Collections.emptyList();
        	    }        	    
        		return sensorDataRepository.findBySensorIdInAndRecordCreatedAtBetween(validSensorsIds, from, to)
        	                .stream()
        	                .map(SensorDataDataAdapter::toDTO)
        	                .collect(Collectors.toList());
        	}
        } return Collections.emptyList();
    }

}
