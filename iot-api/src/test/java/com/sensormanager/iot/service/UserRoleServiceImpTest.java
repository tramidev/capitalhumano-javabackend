package com.sensormanager.iot.service;

import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.model.*;
import com.sensormanager.iot.repository.RoleRepository;
import com.sensormanager.iot.repository.UserRepository;
import com.sensormanager.iot.repository.UserRoleRepository;
import com.sensormanager.iot.security.AuthenticatedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.lenient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImpTest {

    @InjectMocks
    private UserRoleServiceImp userRoleService;

    @Mock
    private AuthenticatedService authenticatedService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Test
    void shouldRejectRootAssignmentWithoutCompany() {
        Role rootRole = Role.builder().id(1L).roleName("ROOT").build();
        User user = User.builder().id(100L).company(null).build();
        Company currentCompany = Company.builder().id(1L).companyName("Empresa A").build();

        when(authenticatedService.isRootUser()).thenReturn(true);
        when(authenticatedService.getAuthenticatedCompany()).thenReturn(currentCompany);
        when(roleRepository.findById(1L)).thenReturn(rootRole);
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));

        UserRoleDTO dto = new UserRoleDTO(100, 1);

        assertThrows(ResponseStatusException.class, () -> {
            userRoleService.create(dto);
        });
    }

    @Test
    void shouldRejectRootAssignmentToOtherCompany() {
        Role rootRole = Role.builder().id(1L).roleName("ROOT").build();
        Company empresaB = Company.builder().id(2L).companyName("Empresa B").build();
        Company empresaA = Company.builder().id(1L).companyName("Empresa A").build();

        User user = User.builder().id(101L).company(empresaB).build();

        when(authenticatedService.isRootUser()).thenReturn(true);
        when(authenticatedService.getAuthenticatedCompany()).thenReturn(empresaA);
        when(roleRepository.findById(1L)).thenReturn(rootRole);
        when(userRepository.findById(101L)).thenReturn(Optional.of(user));

        UserRoleDTO dto = new UserRoleDTO(101, 1);

        assertThrows(ResponseStatusException.class, () -> {
            userRoleService.create(dto);
        });
    }

    @Test
    void shouldFailIfRoleDoesNotExist() {
        when(authenticatedService.isRootUser()).thenReturn(true);
        when(roleRepository.findById(99L)).thenReturn(null);

        UserRoleDTO dto = new UserRoleDTO(100, 99);

        assertThrows(ResponseStatusException.class, () -> {
            userRoleService.create(dto);
        });
    }

    @Test
    void shouldFailIfTargetUserDoesNotExist() {
        Role role = Role.builder().id(2L).roleName("COMPANY_USER").build();

        when(authenticatedService.isRootUser()).thenReturn(true);
        when(roleRepository.findById(2L)).thenReturn(role);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserRoleDTO dto = new UserRoleDTO(999, 2);

        assertThrows(ResponseStatusException.class, () -> {
            userRoleService.create(dto);
        });
    }

    @Test
    void shouldFailIfRoleAlreadyAssigned() {
        Role role = Role.builder().id(2L).roleName("COMPANY_USER").build();
        User user = User.builder().id(100L).company(
                Company.builder().id(1L).companyName("Empresa A").build()
        ).build();

        UserRoleId existingId = new UserRoleId(100, 2);

        when(authenticatedService.isRootUser()).thenReturn(true);
        when(roleRepository.findById(2L)).thenReturn(role);
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(userRoleRepository.existsById(existingId)).thenReturn(true);

        UserRoleDTO dto = new UserRoleDTO(100, 2);

        assertThrows(ResponseStatusException.class, () -> {
            userRoleService.create(dto);
        });
    }

    @Test
    void shouldFailIfUserIsDisabledWhenAssigningRole() {
        Role role = Role.builder().id(4L).roleName("COMPANY_USER").build();
        Company company = Company.builder().id(1L).companyName("Empresa A").build();

        User disabledUser = User.builder()
                .id(110L)
                .company(company)
                .userStatus(false) // 🔥 deshabilitado
                .build();

        when(authenticatedService.isRootUser()).thenReturn(true);
        when(roleRepository.findById(4L)).thenReturn(role);
        when(userRepository.findById(110L)).thenReturn(Optional.of(disabledUser));

        UserRoleDTO dto = new UserRoleDTO(110, 4);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userRoleService.create(dto);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertTrue(exception.getReason().contains("disabled user"));
    }

    @Test
    void shouldFailUpdateIfUserIsDisabled() {
        Role role = Role.builder().id(4L).roleName("COMPANY_USER").build();
        Company company = Company.builder().id(1L).companyName("Empresa A").build();

        User disabledUser = User.builder()
                .id(120L)
                .company(company)
                .userStatus(false)
                .build();

        UserRole existingRole = UserRole.builder()
                .id(new UserRoleId(120, 2))
                .build();

        lenient().when(authenticatedService.isRootUser()).thenReturn(true);
        when(roleRepository.findById(4L)).thenReturn(role);
        when(userRepository.findById(120L)).thenReturn(Optional.of(disabledUser));
        lenient().when(userRepository.findById(8L)).thenReturn(Optional.of(disabledUser));

        UserRoleDTO dto = new UserRoleDTO(120, 4);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userRoleService.update(dto);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertTrue(exception.getReason().toLowerCase().contains("disabled"));
    }

}