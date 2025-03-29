package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.SensorDataAdapter;
import com.sensormanager.iot.dto.SensorDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.SensorRepository;
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

    @Override
	public List<SensorDTO> findAll() {
        List<Sensor> sensors = sensorRepository.findBySensorStatusTrue();
        if (sensors == null) {
            return new ArrayList<SensorDTO>();
        }
        return sensors.stream().map(SensorDataAdapter::toDTO).collect(Collectors.toList());
    }

    @Override
    public SensorDTO findById(Long id) {
        Sensor sensor = sensorRepository.findByIdAndSensorStatusTrue(id).orElse(null);
        if (sensor == null) {
            return null;
        }
        return SensorDataAdapter.toDTO(sensor);
    }

    @Override
    public List<SensorDTO> findByCompany(Long id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            return new ArrayList<>();
        }

        List<Sensor> sensors = sensorRepository.findBySensorCompany(company);
        if (sensors == null) {
            return new ArrayList<>();
        }

        return sensors.stream()
                .map(SensorDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
	public SensorDTO create(SensorDTO sensorDTO) {
		if (sensorRepository.existsBySensorName(sensorDTO.getSensorName())) {
            return new SensorDTO();
        }		
		sensorDTO.setSensorApiKey(GenerateApiKey.generate(sensorDTO.getSensorName()));
		sensorDTO.setSensorStatus(true);
        Sensor sensor = SensorDataAdapter.toEntity(sensorDTO);
        Sensor sensorAdded = sensorRepository.save(sensor);
        
        if (sensorAdded == null) {
            return new SensorDTO();
        }
        return SensorDataAdapter.toDTO(sensorAdded);
	}

	@Override
	public SensorDTO update(SensorDTO sensorDto) {
		
		Sensor sensorWillUpdated = sensorRepository.findById(sensorDto.getId()).get();		
		if (sensorWillUpdated == null) {
            return new SensorDTO();
        }
		sensorWillUpdated.setSensorName(sensorDto.getSensorName() != null && sensorDto.getSensorName().length() > 0 ? sensorDto.getSensorName() : sensorWillUpdated.getSensorName());
		sensorWillUpdated.setSensorStatus(sensorDto.getSensorStatus());
        Sensor sensorUpdated = sensorRepository.save(sensorWillUpdated);
        if (sensorUpdated == null) {
            return new SensorDTO();
        }
        return SensorDataAdapter.toDTO(sensorUpdated);
	}

	@Override
	public SensorDTO deleteById(Long id) {
		Sensor sensorWillDeleted = sensorRepository.findById(id).get();
        if (sensorWillDeleted == null) {
            return new SensorDTO();
        }
        sensorWillDeleted.setSensorStatus(false);
        Sensor sensorDeleted= sensorRepository.save(sensorWillDeleted);
        if (sensorDeleted == null) {
            return new SensorDTO();
        }
        return SensorDataAdapter.toDTO(sensorDeleted);
	}

}
