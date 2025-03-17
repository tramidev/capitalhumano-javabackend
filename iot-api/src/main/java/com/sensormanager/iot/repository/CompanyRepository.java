package com.sensormanager.iot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sensormanager.iot.model.Company;


@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer>{

	boolean existsByCompanyName(String companyName);
	boolean existsById(Long companyId);
	List<Company> findByCompanyStatusTrue();
	Optional<Company> findById(Long companyId);
	Optional<Company> findByIdAndCompanyStatusTrue(Long companyId);
	boolean deleteById(Long companyId);
}
