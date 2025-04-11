package com.sensormanager.iot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sensormanager.iot.model.UserRole;
import com.sensormanager.iot.model.UserRoleId;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    Optional<UserRole> findByIdUserId(Integer userId);
}
