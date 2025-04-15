package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.SensorDataAdapter;
import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.SensorRepository;
import com.sensormanager.iot.security.AuthenticatedService;
import com.sensormanager.iot.utils.GenerateApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        List<Sensor> sensors = authenticatedService.isRootUser()
                ? sensorRepository.findBySensorStatusTrue()
                : sensorRepository.findBySensorCompanyAndSensorStatusTrue(authenticatedService.getAuthenticatedCompany());

        return sensors.stream()
                .map(SensorDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SensorDTO findById(Long id) {
        Sensor sensor = sensorRepository.findByIdAndSensorStatusTrue(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found."));

        if (!authenticatedService.isRootUser() &&
                !sensor.getSensorCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to sensor.");
        }

        return SensorDataAdapter.toDTO(sensor);
    }

    @Override
    public List<SensorDTO> findByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found."));

        if (!authenticatedService.isRootUser() &&
                !company.getId().equals(authenticatedService.getUserCompanyId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to company sensors.");
        }

        List<Sensor> sensors = sensorRepository.findBySensorCompanyAndSensorStatusTrue(company);

        if (sensors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No sensors found for the given company.");
        }

        return sensors.stream()
                .map(SensorDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SensorDTO create(SensorDTO sensorDTO) {
        if (sensorDTO.getSensorName() == null || sensorDTO.getSensorName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sensor name is required.");
        }

        if (sensorRepository.existsBySensorName(sensorDTO.getSensorName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sensor name already exists.");
        }

        Company company = authenticatedService.isRootUser()
                ? companyRepository.findById(sensorDTO.getSensorCompany())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found."))
                : authenticatedService.getAuthenticatedCompany();

        if (company == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated company found.");
        }

        sensorDTO.setSensorApiKey(GenerateApiKey.generate(sensorDTO.getSensorName()));
        sensorDTO.setSensorStatus(true);
        sensorDTO.setSensorCompany(company.getId());

        Sensor sensor = SensorDataAdapter.toEntity(sensorDTO);
        sensor.setSensorCompany(company);

        Sensor sensorSaved = sensorRepository.save(sensor);

        return SensorDataAdapter.toDTO(sensorSaved);
    }

    @Override
    public SensorDTO update(SensorDTO sensorDTO) {
        Sensor sensor = sensorRepository.findById(sensorDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found."));

        if (!authenticatedService.isRootUser() &&
                !sensor.getSensorCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to sensor.");
        }

        if (sensorDTO.getSensorName() != null && !sensorDTO.getSensorName().isEmpty()) {
            sensor.setSensorName(sensorDTO.getSensorName());
        }

        if (sensorDTO.getSensorStatus() != null) {
            sensor.setSensorStatus(sensorDTO.getSensorStatus());
        }

        Sensor updated = sensorRepository.save(sensor);
        return SensorDataAdapter.toDTO(updated);
    }

    @Override
    public SensorDTO deleteById(Long id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found."));

        if (!authenticatedService.isRootUser() &&
                !sensor.getSensorCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to sensor.");
        }

        sensor.setSensorStatus(false);
        Sensor deleted = sensorRepository.save(sensor);
        return SensorDataAdapter.toDTO(deleted);
    }
}
