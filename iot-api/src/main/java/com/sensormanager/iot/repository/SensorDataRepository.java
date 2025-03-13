package com.sensormanager.iot.repository;

import com.sensormanager.iot.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    // Buscar el identificador del sensor
    List<SensorData> findBySensorId(Long sensorId);
}
