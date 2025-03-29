package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.RoleDTO;
import com.sensormanager.iot.model.Role;

public class RoleDataAdapter {

    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getRoleName(),
                role.getRoleDescription(),
                role.getRoleCreatedAt()
        );
    }

    public static Role toEntity(RoleDTO roleDTO) {
        return new Role(
                roleDTO.getId(),
                roleDTO.getRoleName(),
                roleDTO.getRoleDescription(),
                roleDTO.getRoleCreatedAt()
        );
    }

}
