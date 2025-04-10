package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.SensorDataAdapter;
import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.SensorRepository;
import com.sensormanager.iot.security.AuthenticatedService;
import com.sensormanager.iot.security.CustomUserSecurity;
import com.sensormanager.iot.utils.GenerateApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorServiceImp implements SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AuthenticatedService authenticatedService;

    @Override
    public List<SensorDTO> findAll() {
        List<Sensor> sensors;
        if (authenticatedService.isRootUser()) {
            sensors = sensorRepository.findBySensorStatusTrue();
        } else {
            Company company = authenticatedService.getAuthenticatedCompany();
            sensors = sensorRepository.findBySensorCompanyAndSensorStatusTrue(company);

        }
        if (sensors == null) return new ArrayList<>();
        return sensors.stream().map(SensorDataAdapter::toDTO).collect(Collectors.toList());
    }

    @Override
    public SensorDTO findById(Long id) {
        Sensor sensor = sensorRepository.findByIdAndSensorStatusTrue(id).orElse(null);
        if (sensor == null) return null;

        if (!authenticatedService.isRootUser() &&
                !sensor.getSensorCompany().getId().equals(authenticatedService.getAuthenticatedCompany().getId())) {
            return new SensorDTO();
        }

        return SensorDataAdapter.toDTO(sensor);
    }

    @Override
    public List<SensorDTO> findByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) return new ArrayList<>();

        if (!authenticatedService.isRootUser() &&
                !company.getId().equals(authenticatedService.getAuthenticatedCompany().getId())) {
            return new ArrayList<>();
        }

        List<Sensor> sensors = sensorRepository.findBySensorCompanyAndSensorStatusTrue(company);
        if (sensors == null) return new ArrayList<>();
        return sensors.stream().map(SensorDataAdapter::toDTO).collect(Collectors.toList());
    }

    @Override
    public SensorDTO create(SensorDTO sensorDTO) {
        if (sensorRepository.existsBySensorName(sensorDTO.getSensorName())) {
            return new SensorDTO();
        }

        Company company;
        if (authenticatedService.isRootUser()) {
            company = companyRepository.findById(sensorDTO.getSensorCompany()).orElse(null);
            if (company == null) return new SensorDTO();
        } else {
            company = authenticatedService.getAuthenticatedCompany();
        }

        sensorDTO.setSensorApiKey(GenerateApiKey.generate(sensorDTO.getSensorName()));
        sensorDTO.setSensorStatus(true);
        sensorDTO.setSensorCompany(company.getId());

        Sensor sensor = SensorDataAdapter.toEntity(sensorDTO);
        sensor.setSensorCompany(company);
        Sensor sensorAdded = sensorRepository.save(sensor);

        return sensorAdded != null ? SensorDataAdapter.toDTO(sensorAdded) : new SensorDTO();
    }

    @Override
    public SensorDTO update(SensorDTO sensorDTO) {
        Sensor sensor = sensorRepository.findById(sensorDTO.getId()).orElse(null);
        if (sensor == null) return new SensorDTO();

        if (!authenticatedService.isRootUser() &&
                !sensor.getSensorCompany().getId().equals(authenticatedService.getAuthenticatedCompany().getId())) {
            return new SensorDTO();
        }

        sensor.setSensorName(
                sensorDTO.getSensorName() != null && !sensorDTO.getSensorName().isEmpty()
                        ? sensorDTO.getSensorName()
                        : sensor.getSensorName()
        );
        sensor.setSensorStatus(sensorDTO.getSensorStatus());

        Sensor sensorUpdated = sensorRepository.save(sensor);
        return sensorUpdated != null ? SensorDataAdapter.toDTO(sensorUpdated) : new SensorDTO();
    }

    @Override
    public SensorDTO deleteById(Long id) {
        Sensor sensor = sensorRepository.findById(id).orElse(null);
        if (sensor == null) return new SensorDTO();

        if (!authenticatedService.isRootUser() &&
                !sensor.getSensorCompany().getId().equals(authenticatedService.getAuthenticatedCompany().getId())) {
            return new SensorDTO();
        }

        sensor.setSensorStatus(false);
        Sensor sensorDeleted = sensorRepository.save(sensor);

        return sensorDeleted != null ? SensorDataAdapter.toDTO(sensorDeleted) : new SensorDTO();
    }
}
