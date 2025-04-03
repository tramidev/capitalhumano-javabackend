package com.sensormanager.iot.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDataDTO {
    private Long id;
    private Long idSensor;
    private String metric;
    private String record;
    private Long recordCreatedAt;
}
