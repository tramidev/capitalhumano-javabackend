package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.model.UserRole;

public class UserRoleDataAdapter {

    public static UserRoleDTO toDTO(UserRole userRole) {
        return UserRoleDTO.builder()
                .userId(userRole.getUserId())
                .roleId(userRole.getRoleId())
                .build();
    }

    public static UserRole toEntity(UserRoleDTO userDTO) {
        return UserRole.builder()
                .userId(userDTO.getUserId())
                .roleId(userDTO.getRoleId())
                .build();
    }
}
