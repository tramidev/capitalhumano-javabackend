package com.sensormanager.iot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sensormanager.iot.adapter.RoleDataAdapter;
import com.sensormanager.iot.dto.RoleDTO;
import com.sensormanager.iot.model.Role;
import com.sensormanager.iot.repository.RoleRepository;
import com.sensormanager.iot.security.AuthenticatedService;

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
        return (role == null || role.getId() == null)
                ? new RoleDTO()
                : RoleDataAdapter.toDTO(role);
    }

    @Override
    public RoleDTO create(RoleDTO roleDTO) {
        if (!authenticatedService.isRootUser()) {
            return new RoleDTO();
        }

        Role role = RoleDataAdapter.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return RoleDataAdapter.toDTO(savedRole);
    }

    @Override
    public RoleDTO update(RoleDTO roleDto) {
        if (!authenticatedService.isRootUser()) {
            return new RoleDTO();
        }

        Role role = roleRepository.findById(roleDto.getId());
        if (role == null) return new RoleDTO();

        role.setRoleName(roleDto.getRoleName());
        role.setRoleDescription(roleDto.getRoleDescription());

        Role updated = roleRepository.save(role);
        return RoleDataAdapter.toDTO(updated);
    }

    @Override
    public RoleDTO deleteById(Long id) {
        if (!authenticatedService.isRootUser()) {
            return new RoleDTO();
        }

        Role role = roleRepository.findById(id);
        if (role != null) {
            roleRepository.delete(role);
        }

        return role != null ? RoleDataAdapter.toDTO(role) : new RoleDTO();
    }
}
