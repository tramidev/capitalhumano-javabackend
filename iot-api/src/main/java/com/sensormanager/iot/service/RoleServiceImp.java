package com.sensormanager.iot.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sensormanager.iot.adapter.RoleDataAdapter;
import com.sensormanager.iot.dto.RoleDTO;
import com.sensormanager.iot.model.Role;
import com.sensormanager.iot.repository.RoleRepository;

@Service
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(RoleDataAdapter::toDTO).collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id);
        if (role == null || role.getId() == null) {
            return new RoleDTO();
        }
        return RoleDataAdapter.toDTO(role);
    }

    @Override
    public RoleDTO create(RoleDTO roleDTO) {
        Role role = RoleDataAdapter.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return RoleDataAdapter.toDTO(savedRole);
    }

    @Override
    public RoleDTO update(RoleDTO roleDto) {
        Role roleToUpdate = roleRepository.findById(roleDto.getId());
        if (roleToUpdate == null) {
            return new RoleDTO();
        }
        roleToUpdate.setRoleName(roleDto.getRoleName());
        roleToUpdate.setRoleDescription(roleDto.getRoleDescription());
        Role roleUpdated = roleRepository.save(roleToUpdate);
        return RoleDataAdapter.toDTO(roleUpdated);
    }

    @Override
    public RoleDTO deleteById(Long id) {
        Role roleDelete = roleRepository.findById(id);
        if (roleDelete != null) {
            roleRepository.delete(roleDelete);
        }
        return RoleDataAdapter.toDTO(roleDelete);
    }

}
