package com.sensormanager.iot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sensormanager.iot.adapter.RoleDataAdapter;
import com.sensormanager.iot.dto.RoleDTO;
import com.sensormanager.iot.model.Role;
import com.sensormanager.iot.repository.RoleRepository;
import com.sensormanager.iot.security.AuthenticatedService;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticatedService authenticatedService;

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id);
        if (role == null || role.getId() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The role ID: " + id + " does not exist.");
        }

        return RoleDataAdapter.toDTO(role);
    }

    @Override
    public RoleDTO create(RoleDTO roleDTO) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to create roles.");
        }

        Role role = RoleDataAdapter.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        if (savedRole == null || savedRole.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role could not be created.");
        }

        return RoleDataAdapter.toDTO(savedRole);
    }

    @Override
    public RoleDTO update(RoleDTO roleDto) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update roles.");
        }

        Role role = roleRepository.findById(roleDto.getId());
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The role to update was not found.");
        }

        role.setRoleName(roleDto.getRoleName());
        role.setRoleDescription(roleDto.getRoleDescription());

        Role updated = roleRepository.save(role);
        if (updated == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role could not be updated.");
        }

        return RoleDataAdapter.toDTO(updated);
    }

    @Override
    public RoleDTO deleteById(Long id) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete roles.");
        }

        Role role = roleRepository.findById(id);
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The role to delete was not found.");
        }

        roleRepository.delete(role);
        return RoleDataAdapter.toDTO(role);
    }
}
