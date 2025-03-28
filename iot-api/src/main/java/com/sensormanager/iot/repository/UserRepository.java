package com.sensormanager.iot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensormanager.iot.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findByUsername(String username);
    User findByUserEmail(String userEmail);
    User deleteById(Long id);    
    User findById(Long id);

}
