package com.sensormanager.iot.repository;

import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByLocationStatusTrue();
    List<Location> findByCompanyAndLocationStatusTrue(Company company);
    Optional<Location> findByIdAndLocationStatusTrue(Long id);
    Optional<Location> findByIdAndLocationStatusTrueAndCompany(Long id, Company company);
    Optional<Location> findByIdAndCompany(Long id, Company company);
    boolean existsByLocationNameAndCompany(String locationName, Company company);
    void deleteById(Long id);
}
