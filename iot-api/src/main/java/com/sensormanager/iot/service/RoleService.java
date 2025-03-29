package com.sensormanager.iot.service;

import java.util.List;

import com.sensormanager.iot.dto.RoleDTO;

public interface RoleService {

    List<RoleDTO> findAll();
    RoleDTO findById(Long Id);
    RoleDTO create(RoleDTO roleDto);
    RoleDTO update(RoleDTO roleDto);
    RoleDTO deleteById(Long id);

}
