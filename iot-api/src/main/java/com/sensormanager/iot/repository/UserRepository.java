package com.sensormanager.iot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
	
    List<User> findByCompany(Company company);
    Optional<User> findByUsername(String username);
    Optional<User> findByUserEmail(String userEmail);   
    Optional<User> findById(Long id);
    Optional<User> findByIdAndCompany(Long id, Company company);

}
