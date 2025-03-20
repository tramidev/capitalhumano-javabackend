package com.sensormanager.iot.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LocationDTO {

    private Long id;
    private String locationName;
    private String locationCountry;
    private String locationCity;
    private String locationStreet;
    private String locationNumber;
    private Boolean locationStatus;
    private Long locationCreatedAt;
    private Long companyId;
}
