package com.sensormanager.iot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sensormanager.iot.adapter.UserRoleDataAdapter;
import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.model.UserRole;
import com.sensormanager.iot.repository.UserRoleRepository;

@Service
public class UserRoleServiceImp implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserRoleDTO> findAll() {
        List<UserRole> userRoles = userRoleRepository.findAll();
        return userRoles.stream().map(UserRoleDataAdapter::toDTO).collect(Collectors.toList());
    }

    @Override
    public UserRoleDTO findByUserId(Integer userId) {
        UserRole userRole = userRoleRepository.findByUserId(userId);
        if (userRole == null || userRole.getUserId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The User Role ID: " + userId + " does not exist."
            );
        }
        return UserRoleDataAdapter.toDTO(userRole);
    }

    @Override
    public UserRoleDTO create(UserRoleDTO userRoleDto) {
        UserRole userRole = UserRoleDataAdapter.toEntity(userRoleDto);
        UserRole saveUserRole = userRoleRepository.save(userRole);
        return UserRoleDataAdapter.toDTO(saveUserRole);
    }

    @Override
    public UserRoleDTO update(UserRoleDTO userRoleDto) {
        UserRole userRoleToUpdate = userRoleRepository.findByUserId(userRoleDto.getUserId());
        if (userRoleToUpdate == null) {
            return new UserRoleDTO();
        }
        userRoleToUpdate.setUserId(userRoleDto.getUserId());
        userRoleToUpdate.setRoleId(userRoleDto.getRoleId());
        UserRole userRoleUpdated = userRoleRepository.save(userRoleToUpdate);
        return UserRoleDataAdapter.toDTO(userRoleUpdated);
    }

    @Override
    public UserRoleDTO deleteById(Integer userId) {
        UserRole userRoleDelete = userRoleRepository.findByUserId(userId);
        if (userRoleDelete != null) {
            userRoleRepository.delete(userRoleDelete);
        }
        return UserRoleDataAdapter.toDTO(userRoleDelete);
    }

    @Override
    public UserRoleDTO findById(Integer userId) {
        UserRole userRole = userRoleRepository.findByUserId(userId);
        if (userRole == null || userRole.getUserId() == null) {
            return new UserRoleDTO();
        }
        return UserRoleDataAdapter.toDTO(userRole);
    }

}
