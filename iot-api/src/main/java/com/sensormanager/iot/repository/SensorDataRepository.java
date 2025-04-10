package com.sensormanager.iot.repository;

import com.sensormanager.iot.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

	List<SensorData> findBySensorIdInAndRecordCreatedAtBetween(List<Long> sensorIds, Long from, Long to);
}
