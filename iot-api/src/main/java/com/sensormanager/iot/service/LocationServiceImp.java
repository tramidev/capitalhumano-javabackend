package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.LocationDataAdapter;
import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Location;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.LocationRepository;
import com.sensormanager.iot.repository.SensorRepository;
import com.sensormanager.iot.security.AuthenticatedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImp implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private AuthenticatedService authenticatedService;

    @Override
    public List<LocationDTO> findAll() {
        if (authenticatedService.isRootUser()) {
            return locationRepository.findByLocationStatusTrue()
                    .stream().map(LocationDataAdapter::toDTO).collect(Collectors.toList());
        }

        Company company = authenticatedService.getAuthenticatedCompany();
        return locationRepository.findByCompanyAndLocationStatusTrue(company)
                .stream().map(LocationDataAdapter::toDTO).collect(Collectors.toList());
    }

    @Override
    public LocationDTO findById(Long id) {
        if (authenticatedService.isRootUser()) {
            return locationRepository.findByIdAndLocationStatusTrue(id)
                    .map(LocationDataAdapter::toDTO).orElse(new LocationDTO());
        }

        Company company = authenticatedService.getAuthenticatedCompany();
        return locationRepository.findByIdAndLocationStatusTrueAndCompany(id, company)
                .map(LocationDataAdapter::toDTO).orElse(new LocationDTO());
    }

    @Override
    public LocationDTO create(LocationDTO locationDTO) {
        Company company;

        if (authenticatedService.isRootUser()) {
            company = companyRepository.findById(locationDTO.getCompanyId()).orElse(null);
        } else {
            company = authenticatedService.getAuthenticatedCompany();
        }

        if (company == null) return new LocationDTO();

        Location location = LocationDataAdapter.toEntity(locationDTO);
        location.setCompany(company);
        location.setLocationStatus(true);

        Location locationSaved = locationRepository.save(location);
        return locationSaved != null ? LocationDataAdapter.toDTO(locationSaved) : new LocationDTO();
    }

    @Override
    public LocationDTO update(LocationDTO locationDTO) {
        Location location = locationRepository.findByIdAndLocationStatusTrue(locationDTO.getId()).orElse(null);
        if (location == null) return new LocationDTO();

        if (!authenticatedService.isRootUser() &&
                !location.getCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            return new LocationDTO();
        }

        location.setLocationName(locationDTO.getLocationName() != null ? locationDTO.getLocationName() : location.getLocationName());
        location.setLocationCity(locationDTO.getLocationCity() != null ? locationDTO.getLocationCity() : location.getLocationCity());
        location.setLocationCountry(locationDTO.getLocationCountry() != null ? locationDTO.getLocationCountry() : location.getLocationCountry());
        location.setLocationStreet(locationDTO.getLocationStreet() != null ? locationDTO.getLocationStreet() : location.getLocationStreet());
        location.setLocationNumber(locationDTO.getLocationNumber() != null ? locationDTO.getLocationNumber() : location.getLocationNumber());
        location.setLocationStatus(locationDTO.getLocationStatus() != null ? locationDTO.getLocationStatus() : location.getLocationStatus());

        Location updated = locationRepository.save(location);
        return updated != null ? LocationDataAdapter.toDTO(updated) : new LocationDTO();
    }

    @Override
    public LocationDTO deleteById(Long id) {
        Location location = locationRepository.findById(id).orElse(null);
        if (location == null) return new LocationDTO();

        if (!authenticatedService.isRootUser() &&
                !location.getCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            return new LocationDTO();
        }

        // Desactivar sensores
        List<Sensor> sensors = sensorRepository.findBySensorLocation(location);
        sensors.forEach(sensor -> {
            sensor.setSensorStatus(false);
            sensorRepository.save(sensor);
        });

        // Desactivar locations
        location.setLocationStatus(false);
        Location saved = locationRepository.save(location);

        if (saved == null || Boolean.TRUE.equals(saved.getLocationStatus())) {
            throw new RuntimeException("The location was not disabled.");
        }

        return LocationDataAdapter.toDTO(saved);
    }
}
