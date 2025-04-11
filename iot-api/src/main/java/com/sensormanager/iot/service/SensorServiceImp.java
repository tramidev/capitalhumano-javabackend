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
import org.springframework.stereotype.Service;

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
        Optional<Sensor> optSensor = sensorRepository.findByIdAndSensorStatusTrue(id);
        if (optSensor.isEmpty()) return new SensorDTO();

        Sensor sensor = optSensor.get();
        if (!authenticatedService.isRootUser()
                && !sensor.getSensorCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            return new SensorDTO();
        }

        return SensorDataAdapter.toDTO(sensor);
    }

    @Override
    public List<SensorDTO> findByCompany(Long companyId) {
        Optional<Company> optCompany = companyRepository.findById(companyId);
        if (optCompany.isEmpty()) return Collections.emptyList();

        Company company = optCompany.get();
        if (!authenticatedService.isRootUser()
                && !company.getId().equals(authenticatedService.getUserCompanyId())) {
            return Collections.emptyList();
        }

        return sensorRepository.findBySensorCompanyAndSensorStatusTrue(company)
                .stream()
                .map(SensorDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SensorDTO create(SensorDTO sensorDTO) {
        System.out.println("🔧 [DEBUG] Intentando crear sensor con nombre: " + sensorDTO.getSensorName());

        if (sensorRepository.existsBySensorName(sensorDTO.getSensorName())) {
            System.out.println("⚠️ [DEBUG] Ya existe un sensor con ese nombre");
            return new SensorDTO();
        }

        Company company = authenticatedService.isRootUser()
                ? companyRepository.findById(sensorDTO.getSensorCompany()).orElse(null)
                : authenticatedService.getAuthenticatedCompany();

        if (company == null) {
            System.out.println("❌ [DEBUG] No se encontró la compañía asociada al usuario");
            return new SensorDTO();
        }

        sensorDTO.setSensorApiKey(GenerateApiKey.generate(sensorDTO.getSensorName()));
        sensorDTO.setSensorStatus(true);
        sensorDTO.setSensorCompany(company.getId());

        Sensor sensor = SensorDataAdapter.toEntity(sensorDTO);
        sensor.setSensorCompany(company);

        Sensor sensorSaved = sensorRepository.save(sensor);

        if (sensorSaved == null) {
            System.out.println("❌ [DEBUG] El sensor no se guardó correctamente (sensorSaved es null)");
            return new SensorDTO();
        }

        System.out.println("✅ [DEBUG] Sensor creado exitosamente con ID: " + sensorSaved.getId());
        return SensorDataAdapter.toDTO(sensorSaved);
    }

    @Override
    public SensorDTO update(SensorDTO sensorDTO) {
        Optional<Sensor> optSensor = sensorRepository.findById(sensorDTO.getId());
        if (optSensor.isEmpty()) return new SensorDTO();

        Sensor sensor = optSensor.get();

        if (!authenticatedService.isRootUser()
                && !sensor.getSensorCompany().getId().equals(authenticatedService.getUserCompanyId())) {
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
        Optional<Sensor> optSensor = sensorRepository.findById(id);
        if (optSensor.isEmpty()) return new SensorDTO();

        Sensor sensor = optSensor.get();

        if (!authenticatedService.isRootUser()
                && !sensor.getSensorCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            return new SensorDTO();
        }

        sensor.setSensorStatus(false);
        Sensor sensorDeleted = sensorRepository.save(sensor);
        return sensorDeleted != null ? SensorDataAdapter.toDTO(sensorDeleted) : new SensorDTO();
    }
}
