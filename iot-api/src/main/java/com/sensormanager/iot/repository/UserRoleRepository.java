package com.sensormanager.iot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sensormanager.iot.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    UserRole findByUserId(Integer userId);
    UserRole findByRoleId(Integer roleId);

}
