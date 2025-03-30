package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.LocationDataAdapter;
import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.CustomUserSecurity;
import com.sensormanager.iot.model.Location;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.LocationRepository;
import com.sensormanager.iot.repository.SensorRepository;
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


    @Override
    public List<LocationDTO> findAll() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (userAuth.hasRole("ROOT")) {
        		List<Location> locations = locationRepository.findByLocationStatusTrue();
                if (locations == null) return new ArrayList<>();
                return locations.stream().map(LocationDataAdapter::toDTO).collect(Collectors.toList());
        	} else {
        		List<Location> locations = locationRepository.findByCompanyAndLocationStatusTrue(userAuth.getCompany());
                if (locations == null) return new ArrayList<>();
                return locations.stream().map(LocationDataAdapter::toDTO).collect(Collectors.toList());
        	}
        } else return new ArrayList<>();        
    }

    @Override
    public LocationDTO findById(Long id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (userAuth.hasRole("ROOT")) {
        		Location location = locationRepository.findByIdAndLocationStatusTrue(id).orElse(null);
                if (location == null) return null;
                return LocationDataAdapter.toDTO(location);
        	} else {
        		Location location = locationRepository.findByIdAndLocationStatusTrueAndCompany(id, userAuth.getCompany()).orElse(null);
                if (location == null) return null;
                return LocationDataAdapter.toDTO(location);
        	}
        } else return null;
    }


    @Override
    public LocationDTO create(LocationDTO locationDTO) {
    	Company company = new Company();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (userAuth.hasRole("ROOT")) {
        		company = companyRepository.findById(locationDTO.getCompanyId()).orElse(null);
        		if (company == null) return new LocationDTO();
        	} else {
        		company = companyRepository.findById(userAuth.getCompany().getId()).orElse(null);
                if (company == null) return new LocationDTO();
        	}
        } else return null;    
        
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
        
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
        	CustomUserSecurity userAuth = (CustomUserSecurity) authentication.getPrincipal();    
        	if (!userAuth.hasRole("ROOT") && location.getCompany().getId() != userAuth.getCompany().getId()) {
        		return new LocationDTO();
        	}
        } else new LocationDTO(); 

        // Desactivar sensores asociados
        List<Sensor> sensores = sensorRepository.findBySensorLocation(location);
        for (Sensor sensor : sensores) {
            sensor.setSensorStatus(false);
            sensorRepository.save(sensor);
        }

        // Desactivar location
        location.setLocationStatus(false);
        Location locationDeleted = locationRepository.save(location);

        if (locationDeleted == null) {
            return new LocationDTO();
        }

        return LocationDataAdapter.toDTO(locationDeleted);
    }
}

