package com.sensormanager.iot.service;

import java.util.List;

import com.sensormanager.iot.dto.UserRoleDTO;

public interface UserRoleService {

    List<UserRoleDTO> findAll();

    UserRoleDTO findById(Integer userId);

    UserRoleDTO create(UserRoleDTO userRoleDto);

    UserRoleDTO update(UserRoleDTO userRoleDto);

    UserRoleDTO deleteById(Integer id);

    UserRoleDTO findByUserId(Integer id);

}
