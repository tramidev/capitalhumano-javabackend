package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.model.UserRole;
import com.sensormanager.iot.model.UserRoleId;

public class UserRoleDataAdapter {

    public static UserRoleDTO toDTO(UserRole userRole) {
        return UserRoleDTO.builder()
                .userId(userRole.getUserId())
                .roleId(userRole.getRoleId())
                .build();
    }

    public static UserRole toEntity(UserRoleDTO dto) {
        return UserRole.builder()
                .id(new UserRoleId(dto.getUserId(), dto.getRoleId()))
                .build();
    }
}
