package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.UserRoleDataAdapter;
import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.model.UserRole;
import com.sensormanager.iot.model.UserRoleId;
import com.sensormanager.iot.repository.UserRoleRepository;
import com.sensormanager.iot.security.AuthenticatedService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImp implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private AuthenticatedService authenticatedService;

    @Override
    public List<UserRoleDTO> findAll() {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        return userRoleRepository.findAll()
                .stream()
                .map(UserRoleDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserRoleDTO findByUserId(Integer userId) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        Optional<UserRole> userRoleOpt = userRoleRepository.findByIdUserId(userId);

        return userRoleOpt.map(UserRoleDataAdapter::toDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "The rol for the user ID: " + userId + " was not found."
                ));
    }

    @Override
    public UserRoleDTO create(UserRoleDTO userRoleDto) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        UserRoleId id = new UserRoleId(userRoleDto.getUserId(), userRoleDto.getRoleId());

        if (userRoleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user already already has an assigned role.");
        }

        UserRole userRole = UserRoleDataAdapter.toEntity(userRoleDto);
        UserRole saved = userRoleRepository.save(userRole);
        return UserRoleDataAdapter.toDTO(saved);
    }

    @Override
    public UserRoleDTO update(UserRoleDTO userRoleDto) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        Optional<UserRole> existingOpt = userRoleRepository.findByIdUserId(userRoleDto.getUserId());
        if (existingOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User role not found.");
        }

        // Eliminamos el rol anterior y creamos uno nuevo con la nueva combinación userId + roleId
        userRoleRepository.delete(existingOpt.get());

        UserRole updated = userRoleRepository.save(UserRoleDataAdapter.toEntity(userRoleDto));
        return UserRoleDataAdapter.toDTO(updated);
    }

    @Override
    public UserRoleDTO deleteById(Integer userId) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        Optional<UserRole> userRoleOpt = userRoleRepository.findByIdUserId(userId);
        if (userRoleOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User role not found.");
        }

        userRoleRepository.delete(userRoleOpt.get());
        return UserRoleDataAdapter.toDTO(userRoleOpt.get());
    }

    @Override
    public UserRoleDTO findById(Integer userId) {
        return findByUserId(userId);
    }
}
