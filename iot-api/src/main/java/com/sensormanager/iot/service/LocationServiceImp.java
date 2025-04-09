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
import com.sensormanager.iot.security.CustomUserSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        } else {
            CustomUserSecurity user = authenticatedService.getAuthenticatedUser();
            return locationRepository.findByCompanyAndLocationStatusTrue(user.getCompany())
                    .stream().map(LocationDataAdapter::toDTO).collect(Collectors.toList());
        }
    }

    @Override
    public LocationDTO findById(Long id) {
        if (authenticatedService.isRootUser()) {
            return locationRepository.findByIdAndLocationStatusTrue(id)
                    .map(LocationDataAdapter::toDTO).orElse(null);
        } else {
            CustomUserSecurity user = authenticatedService.getAuthenticatedUser();
            return locationRepository.findByIdAndLocationStatusTrueAndCompany(id, user.getCompany())
                    .map(LocationDataAdapter::toDTO).orElse(null);
        }
    }


    @Override
    public LocationDTO create(LocationDTO locationDTO) {
        Company company;

        if (authenticatedService.isRootUser()) {
            company = companyRepository.findById(locationDTO.getCompanyId()).orElse(null);
        } else {
            CustomUserSecurity user = authenticatedService.getAuthenticatedUser();
            company = companyRepository.findById(user.getCompany().getId()).orElse(null);
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
        Location locationToUpdate = locationRepository.findByIdAndLocationStatusTrue(locationDTO.getId()).orElse(null);
        if (locationToUpdate == null) return new LocationDTO();

        if (!authenticatedService.isRootUser()) {
            CustomUserSecurity user = authenticatedService.getAuthenticatedUser();
            if (!locationToUpdate.getCompany().getId().equals(user.getCompany().getId())) return new LocationDTO();
        }

        locationToUpdate.setLocationName(locationDTO.getLocationName() != null ? locationDTO.getLocationName() : locationToUpdate.getLocationName());
        locationToUpdate.setLocationCity(locationDTO.getLocationCity() != null ? locationDTO.getLocationCity() : locationToUpdate.getLocationCity());
        locationToUpdate.setLocationCountry(locationDTO.getLocationCountry() != null ? locationDTO.getLocationCountry() : locationToUpdate.getLocationCountry());
        locationToUpdate.setLocationStreet(locationDTO.getLocationStreet() != null ? locationDTO.getLocationStreet() : locationToUpdate.getLocationStreet());
        locationToUpdate.setLocationNumber(locationDTO.getLocationNumber() != null ? locationDTO.getLocationNumber() : locationToUpdate.getLocationNumber());
        locationToUpdate.setLocationStatus(locationDTO.getLocationStatus() != null ? locationDTO.getLocationStatus() : locationToUpdate.getLocationStatus());

        Location locationUpdated = locationRepository.save(locationToUpdate);
        return locationUpdated != null ? LocationDataAdapter.toDTO(locationUpdated) : new LocationDTO();
    }


    @Override
    public LocationDTO deleteById(Long id) {
        Location location = locationRepository.findById(id).orElse(null);
        if (location == null) {
            return new LocationDTO();
        }

        if (!authenticatedService.isRootUser()) {
            CustomUserSecurity user = authenticatedService.getAuthenticatedUser();
            if (!location.getCompany().getId().equals(user.getCompany().getId())) {
                return new LocationDTO();
            }
        }

        // Desactivar sensores asociados
        List<Sensor> sensores = sensorRepository.findBySensorLocation(location);
        sensores.forEach(sensor -> {
            sensor.setSensorStatus(false);
            sensorRepository.save(sensor);
        });

        // Desactivar la ubicación
        location.setLocationStatus(false);
        Location saved = locationRepository.save(location);

        if (saved == null || saved.getLocationStatus()) {
            throw new RuntimeException("The location was not disabled.");
        }

        return LocationDataAdapter.toDTO(saved);
    }
}


