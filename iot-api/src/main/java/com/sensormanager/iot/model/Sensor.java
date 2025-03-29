package com.sensormanager.iot.model;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_name")
    private String sensorName;

    @Column(name = "sensor_api_key")
    private String sensorApiKey;

    //Relación con Location
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_location", nullable = false)
    private Location sensorLocation;

    //Relación con Company
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_company", nullable = false)
    private Company sensorCompany;

    @Column(name = "sensor_created_at")
    private Long sensorCreatedAt;

    @Column(name = "sensor_status")
    private Boolean sensorStatus;

    @PrePersist
    protected void onCreate() {
        this.sensorCreatedAt = Instant.now().getEpochSecond();
    }
}
