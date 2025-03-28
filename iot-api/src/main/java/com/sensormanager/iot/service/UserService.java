package com.sensormanager.iot.service;

import java.util.List;

import com.sensormanager.iot.dto.UserDTO;

public interface UserService {

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO create(UserDTO userDto);

    UserDTO update(UserDTO userDto);

    UserDTO deleteById(Long id);

}
