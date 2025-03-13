package com.sensormanager.iot.dto;

import java.time.LocalDateTime;

public class SensorDataDTO {
    private Long id;
    private String sensorId;
    private Double value;
    private LocalDateTime timestamp;

    public SensorDataDTO() {}

    public SensorDataDTO(Long id, String sensorId, Double value, LocalDateTime timestamp) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
