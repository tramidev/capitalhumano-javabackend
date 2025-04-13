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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                    .map(LocationDataAdapter::toDTO)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
        }

        Company company = authenticatedService.getAuthenticatedCompany();
        return locationRepository.findByIdAndLocationStatusTrueAndCompany(id, company)
                .map(LocationDataAdapter::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Access to location is denied"));
    }

    @Override
    public LocationDTO create(LocationDTO locationDTO) {
        Company company;

        if (authenticatedService.isRootUser()) {
            company = companyRepository.findById(locationDTO.getCompanyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company ID is invalid"));
        } else {
            company = authenticatedService.getAuthenticatedCompany();
        }

        Location location = LocationDataAdapter.toEntity(locationDTO);
        location.setCompany(company);
        location.setLocationStatus(true);

        Location locationSaved = locationRepository.save(location);
        if (locationSaved == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not saved.");
        }

        return LocationDataAdapter.toDTO(locationSaved);
    }

    @Override
    public LocationDTO update(LocationDTO locationDTO) {
        Location location = locationRepository.findByIdAndLocationStatusTrue(locationDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        if (!authenticatedService.isRootUser() &&
                !location.getCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this location.");
        }

        location.setLocationName(locationDTO.getLocationName() != null ? locationDTO.getLocationName() : location.getLocationName());
        location.setLocationCity(locationDTO.getLocationCity() != null ? locationDTO.getLocationCity() : location.getLocationCity());
        location.setLocationCountry(locationDTO.getLocationCountry() != null ? locationDTO.getLocationCountry() : location.getLocationCountry());
        location.setLocationStreet(locationDTO.getLocationStreet() != null ? locationDTO.getLocationStreet() : location.getLocationStreet());
        location.setLocationNumber(locationDTO.getLocationNumber() != null ? locationDTO.getLocationNumber() : location.getLocationNumber());
        location.setLocationStatus(locationDTO.getLocationStatus() != null ? locationDTO.getLocationStatus() : location.getLocationStatus());

        Location updated = locationRepository.save(location);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not updated.");
        }

        return LocationDataAdapter.toDTO(updated);
    }

    @Override
    public LocationDTO deleteById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        if (!authenticatedService.isRootUser() &&
                !location.getCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this location.");
        }

        // Desactivar sensores
        List<Sensor> sensors = sensorRepository.findBySensorLocation(location);
        sensors.forEach(sensor -> {
            sensor.setSensorStatus(false);
            sensorRepository.save(sensor);
        });

        // Desactivar location
        location.setLocationStatus(false);
        Location saved = locationRepository.save(location);

        if (saved == null || Boolean.TRUE.equals(saved.getLocationStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location was not disabled.");
        }

        return LocationDataAdapter.toDTO(saved);
    }
}
