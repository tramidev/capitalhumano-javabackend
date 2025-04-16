package com.sensormanager.iot.service;

import com.sensormanager.iot.adapter.UserRoleDataAdapter;
import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.model.*;
import com.sensormanager.iot.repository.RoleRepository;
import com.sensormanager.iot.repository.UserRepository;
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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

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
        System.out.println("👉 [UserRoleServiceImp] Authenticated as ROOT? " + authenticatedService.isRootUser());

        // Solo ROOT puede asignar roles (ningún otro tipo de usuario accede acá)
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        // Validar que el rol exista
        Role role = roleRepository.findById((long) userRoleDto.getRoleId());
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role does not exist.");
        }

        // Validar que el usuario objetivo exista
        User userTarget = userRepository.findById((long) userRoleDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found."));

        // Validar que el usuario esté habilitado
        if (userTarget.getUserStatus() != null && !userTarget.getUserStatus()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot assign roles to a disabled user.");
        }

        // Validación especial si se intenta asignar ROOT
        if (role.getRoleName().equals("ROOT")) {
            Company targetCompany = userTarget.getCompany();
            Company currentCompany = authenticatedService.getAuthenticatedCompany();

            if (targetCompany == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot assign ROOT to a user without company.");
            }

            if (currentCompany == null || !targetCompany.getId().equals(currentCompany.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot assign ROOT to a user from another company.");
            }
        }

        // Validar que ese rol aún no esté asignado al usuario
        UserRoleId id = new UserRoleId(userRoleDto.getUserId(), userRoleDto.getRoleId());
        if (userRoleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user already has the selected role.");
        }

        // Guardar el nuevo rol
        UserRole userRole = UserRoleDataAdapter.toEntity(userRoleDto);
        UserRole saved = userRoleRepository.save(userRole);
        return UserRoleDataAdapter.toDTO(saved);
    }


    @Override
    public UserRoleDTO update(UserRoleDTO userRoleDto) {
        if (!authenticatedService.isRootUser()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }

        Role role = roleRepository.findById((long) userRoleDto.getRoleId());
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role does not exist.");
        }

        User userTarget = userRepository.findById((long) userRoleDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found."));

        if (userTarget.getUserStatus() != null && !userTarget.getUserStatus()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot assign roles to a disabled user.");
        }

        if (role.getRoleName().equals("ROOT")) {
            Company targetCompany = userTarget.getCompany();
            Company currentCompany = authenticatedService.getAuthenticatedCompany();

            if (targetCompany == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot assign ROOT to a user without company.");
            }

            if (currentCompany == null || !targetCompany.getId().equals(currentCompany.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot assign ROOT to a user from another company.");
            }
        }

        Optional<UserRole> existingOpt = userRoleRepository.findByIdUserId(userRoleDto.getUserId());
        if (existingOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User role not found.");
        }

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
