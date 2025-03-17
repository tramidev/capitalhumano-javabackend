package com.sensormanager.iot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name= "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_country")
    private String locationCountry;

    @Column(name = "location_city")
    private String locationCity;

    @Column(name = "location_street")
    private String locationStreet;

    @Column(name = "location_number")
    private String locationNumber;

    @Column(name = "location_status")
    private Boolean locationStatus;

    @Column(name = "location_created_at")
    private Long locationCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @PrePersist
    protected void onCreate() {
        this.locationCreatedAt = Instant.now().getEpochSecond();
    }
}
