// ✅ UserServiceImp.java
package com.sensormanager.iot.service;

import java.util.List;
import java.util.stream.Collectors;

import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.model.Role;
import com.sensormanager.iot.repository.RoleRepository;
import com.sensormanager.iot.security.AuthenticatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sensormanager.iot.adapter.UserDataAdapter;
import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.User;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.repository.UserRepository;
import com.sensormanager.iot.security.CustomUserSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticatedService authenticatedService;

    @Override
    public List<UserDTO> findAll() {
        if (authenticatedService.isRootUser()) {
            return userRepository.findAll().stream()
                    .map(UserDataAdapter::toDTO)
                    .collect(Collectors.toList());
        }

        Company company = authenticatedService.getAuthenticatedCompany();
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated company found.");
        }

        return userRepository.findByCompany(company).stream()
                .map(UserDataAdapter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        if (authenticatedService.isRootUser()) {
            return userRepository.findById(id)
                    .map(UserDataAdapter::toDTO)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User ID not found."));
        }

        Company company = authenticatedService.getAuthenticatedCompany();
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated company found.");
        }

        return userRepository.findByIdAndCompany(id, company)
                .map(UserDataAdapter::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in your company."));
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        System.out.println("⚡️ Entrando a UserServiceImp.create() con: " + userDTO.getUsername());
        System.out.println("👉 [UserServiceImp] Authenticated as ROOT? " + authenticatedService.isRootUser());

        Company company;

        if (authenticatedService.isRootUser()) {
            company = companyRepository.findById(userDTO.getCompanyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found."));
        } else {
            company = authenticatedService.getAuthenticatedCompany();
            if (company == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No authenticated company found.");
            }
        }

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        User user = UserDataAdapter.toEntity(userDTO);
        user.setCompany(company);
        user.setUserStatus(true);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User saved = userRepository.save(user);

        String roleNameToAssign;

        if (authenticatedService.isRootUser()) {
            if (userDTO.getRoleName() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required for user creation.");
            }
            roleNameToAssign = userDTO.getRoleName();
        } else {
            roleNameToAssign = "COMPANY_USER";
        }

        Role role = roleRepository.findByRoleName(roleNameToAssign);
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found: " + roleNameToAssign);
        }

        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(saved.getId().intValue());
        userRoleDTO.setRoleId(role.getId().intValue());

        userRoleService.create(userRoleDTO);

        return UserDataAdapter.toDTO(saved);
    }

    @Override
    public UserDTO update(UserDTO userDto) {
        User userToUpdate = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        if (!authenticatedService.isRootUser() &&
                !userToUpdate.getCompany().getId().equals(authenticatedService.getUserCompanyId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to update this user.");
        }

        userToUpdate.setFirstName(userDto.getFirstName() != null && !userDto.getFirstName().isEmpty() ? userDto.getFirstName() : userToUpdate.getFirstName());
        userToUpdate.setLastName(userDto.getLastName() != null && !userDto.getLastName().isEmpty() ? userDto.getLastName() : userToUpdate.getLastName());
        userToUpdate.setUsername(userDto.getUsername() != null && !userDto.getUsername().isEmpty() ? userDto.getUsername() : userToUpdate.getUsername());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        userToUpdate.setUserEmail(userDto.getUserEmail() != null && !userDto.getUserEmail().isEmpty() ? userDto.getUserEmail() : userToUpdate.getUserEmail());

        return UserDataAdapter.toDTO(userRepository.save(userToUpdate));
    }

    @Override
    @Transactional
    public UserDTO deleteById(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        if (!authenticatedService.isRootUser()) {
            CustomUserSecurity currentUser = authenticatedService.getAuthenticatedUser();
            if (!userToDelete.getCompany().getId().equals(currentUser.getCompany().getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: You are not authorized to delete this user.");
            }
        }

        userToDelete.setUserStatus(false);
        userRepository.save(userToDelete);
        userRepository.flush();

        User userDeleted = userRepository.findById(id).orElse(null);
        if (userDeleted == null || Boolean.TRUE.equals(userDeleted.getUserStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user was not disabled.");
        }

        return UserDataAdapter.toDTO(userDeleted);
    }
}
