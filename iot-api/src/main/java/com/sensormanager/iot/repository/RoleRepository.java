package com.sensormanager.iot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensormanager.iot.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Role findById(Long id);
    Role findByRoleName(String roleName);
    Boolean deleteById(Long id);

}
