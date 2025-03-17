package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.LocationDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Location;

public class LocationDataAdapter {

    public static LocationDTO toDTO(Location location) {
        return LocationDTO.builder()
                .id(location.getId())
                .locationName(location.getLocationName())
                .locationCountry(location.getLocationCountry())
                .locationCity(location.getLocationCity())
                .locationStreet(location.getLocationStreet())
                .locationNumber(location.getLocationNumber())
                .locationStatus(location.getLocationStatus())
                .locationCreatedAt(location.getLocationCreatedAt())
                .companyId(location.getCompany()!= null ? location.getCompany().getId():null)
                .build();
    }

    public static Location toEntity(LocationDTO dto) {
        return Location.builder()
                .id(dto.getId())
                .locationName(dto.getLocationName())
                .locationCountry(dto.getLocationCountry())
                .locationCity(dto.getLocationCity())
                .locationStreet(dto.getLocationStreet())
                .locationNumber(dto.getLocationNumber())
                .locationStatus(dto.getLocationStatus())
                .locationCreatedAt(dto.getLocationCreatedAt())
                .company(Company.builder().id(dto.getCompanyId()).build())
                .build();
    }
}
