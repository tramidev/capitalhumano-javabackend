package com.sensormanager.iot.repository;

import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Sensor;
import com.sensormanager.iot.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer>{

	boolean existsBySensorName(String sensorName);
	boolean existsById(Long sensorId);
	List<Sensor> findBySensorStatusTrue();
	List<Sensor> findBySensorCompany(Company company);
	List<Sensor> findBySensorLocation(Location location);
	Optional<Sensor> findById(Long sensorId);
	Optional<Sensor> findByIdAndSensorStatusTrue(Long SensorId);
	boolean deleteById(Long sensorId);
	Optional<Sensor> findSensorBySensorApiKey(String apiKey);
}
