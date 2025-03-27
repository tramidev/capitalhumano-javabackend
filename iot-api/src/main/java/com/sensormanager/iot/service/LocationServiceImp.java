package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.LocationDataAdapter;
import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Location;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<LocationDTO> findAll(){
        List<Location> locations = locationRepository.findByLocationStatusTrue();
        if (locations == null) {
            return new ArrayList<>();
        }

        return  locations.stream()
                .map(LocationDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LocationDTO findById(Long id) {
        Location location = locationRepository.findByIdAndLocationStatusTrue(id).orElse(null);
        if (location == null) {
            return null; // devuelve null en vez de un DTO vacío
        }
        return LocationDataAdapter.toDTO(location);
    }


    @Override
    public LocationDTO create (LocationDTO locationDTO) {
        Company company = companyRepository.findById(locationDTO.getCompanyId()).orElse(null);
        if (company == null) {
            return new LocationDTO();
        }
        Location location = LocationDataAdapter.toEntity(locationDTO);
        location.setCompany(company);
        location.setLocationStatus(true);

        Location locationSaved = locationRepository.save(location);
        if (locationSaved == null) {
            return new LocationDTO();
        }
        return LocationDataAdapter.toDTO(locationSaved);
    }

    @Override
    public LocationDTO update(LocationDTO locationDTO) {
        Location locationToUpdate = locationRepository.findByIdAndLocationStatusTrue(locationDTO.getId()).orElse(null);
        if (locationToUpdate == null) {
            return new LocationDTO();
        }

        locationToUpdate.setLocationName(
                locationDTO.getLocationName() != null ? locationDTO.getLocationName() : locationToUpdate.getLocationName()
        );

        locationToUpdate.setLocationCity(
                locationDTO.getLocationCity() != null ? locationDTO.getLocationCity() : locationToUpdate.getLocationCity()
        );

        locationToUpdate.setLocationCountry(
                locationDTO.getLocationCountry() != null ? locationDTO.getLocationCountry() : locationToUpdate.getLocationCountry()
        );

        locationToUpdate.setLocationStreet(
                locationDTO.getLocationStreet() != null ? locationDTO.getLocationStreet() : locationToUpdate.getLocationStreet()
        );

        locationToUpdate.setLocationNumber(
                locationDTO.getLocationNumber() != null ? locationDTO.getLocationNumber() : locationToUpdate.getLocationNumber()
        );

        locationToUpdate.setLocationStatus(
                locationDTO.getLocationStatus() != null ? locationDTO.getLocationStatus() : locationToUpdate.getLocationStatus()
        );

        Location locationUpdated = locationRepository.save(locationToUpdate);

        if (locationUpdated == null) {
            return new LocationDTO();
        }

        return LocationDataAdapter.toDTO(locationUpdated);
    }

    @Override
    public LocationDTO deleteById(Long id) {
        Location location = locationRepository.findById(id).orElse(null);
        if (location == null) {
            return new LocationDTO();
        }

        location.setLocationStatus(false);
        Location locationDeleted = locationRepository.save(location);
        if (locationDeleted == null) {
            return new LocationDTO();
        }

        return LocationDataAdapter.toDTO(locationDeleted);
    }
}
